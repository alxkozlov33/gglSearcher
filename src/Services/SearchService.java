package Services;

import Models.InputCsvModelItem;
import Models.OutputCSVModels.OutputCsvModelItem;
import Models.SearchResultsModels.SearchResult;
import Models.SearchSettings;
import Utils.StrUtils;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.net.Proxy;
import java.util.ArrayList;
import java.util.Random;

import org.jsoup.nodes.Element;

public class SearchService {

    private final FileService fileService;
    private final GuiService guiService;
    private final LogService logService;
    private final PropertiesService propertiesService;
    private final ProxyService proxyService;
    private final UserAgentsRotatorService userAgentsRotatorService;

    private ArrayList<InputCsvModelItem> inputCsvItems;
    private SearchSettings searchExceptions;
    private boolean isWorkFlag = false;

    public SearchService() {
        this.fileService = DIResolver.getFileService();
        this.logService =  DIResolver.getLogService();
        this.guiService = DIResolver.getGuiService();
        this.propertiesService = DIResolver.getPropertiesService();
        this.proxyService = DIResolver.getProxyService();
        this.userAgentsRotatorService = DIResolver.getUserAgentsRotatorService();
    }

    private int getRandomTime() {
        Random r = new Random();
        int low = 10000;
        int high = 15000;
        return r.nextInt(high-low) + low;
    }

    public void DoWork(ArrayList<InputCsvModelItem> inputCsvItems, SearchSettings searchExceptions) {
        this.inputCsvItems = inputCsvItems;
        this.searchExceptions = searchExceptions;
        Thread worker = new Thread(() -> {
            guiService.changeApplicationStateToWork(true);
            isWorkFlag = true;
            StartWork();
            guiService.changeApplicationStateToWork(false);
            logService.LogMessage("Stopped");
            logService.UpdateStatus("Stopped");
        });
        worker.start();
    }

    private void StartWork() {
        try {
            int index = propertiesService.getIndex();
            if (StringUtils.isEmpty(guiService.getSearchPlaceholderText())){
                logService.LogMessage("Placeholder empty");
                return;
            }
            if (searchExceptions == null){
                logService.LogMessage("Exceptions file was not chosen");
                return;
            }
            if (inputCsvItems == null && StrUtils.isPlaceholderHasSubstituteTerms(guiService.getSearchPlaceholderText())) {
                Element body = getQueryBody(null);
                if (body != null) {
                    SearchResult result = new SearchResult(logService)
                            .initSearchExceptions(searchExceptions)
                            .parsePageBody(body);
                    ArrayList<OutputCsvModelItem> items = fileService.mapSearchResultsToOutputCSVModels(result);
                    fileService.SaveResultCsvItems(items);
                }
            } else {
                logService.LogMessage("Continue from: " + index + " record");
                for (int i = index; i < inputCsvItems.size(); i++) {
                    logService.updateCountItemsStatus(i, inputCsvItems.size());
                    if (!isWorkFlag) {
                        break;
                    }
                    propertiesService.saveIndex(i);
                    Element body = getQueryBody(inputCsvItems.get(i));
                    if (body != null) {
                        SearchResult result = new SearchResult(logService)
                                .initCity(StrUtils.getSearchValue(inputCsvItems.get(i), guiService.getSearchPlaceholderText()))
                                .initCountry(inputCsvItems.get(i).getColumnB())
                                .initSearchExceptions(searchExceptions)
                                .parsePageBody(body);
                        ArrayList<OutputCsvModelItem> items = fileService.mapSearchResultsToOutputCSVModels(result);
                        fileService.SaveResultCsvItems(items);
                    }
                }
            }
            logService.LogMessage("Search finished");
            logService.UpdateStatus("Finished");
        } catch (Exception e) {
            isWorkFlag = false;
            guiService.changeApplicationStateToWork(false);
            logService.LogMessage("Application stopped");
            e.printStackTrace();
        }
    }

    public void setWorkStateToStop() {
        isWorkFlag = false;
    }

    private Element getQueryBody(InputCsvModelItem item) {
        if (!isWorkFlag) {
            return null;
        }
        String inputPlaceHolder = StrUtils.createURL(item, guiService.getSearchPlaceholderText());
        if (StringUtils.isEmpty(inputPlaceHolder)) {
            logService.LogMessage("Define search placeholder");
            isWorkFlag = false;
            return null;
        }

        Element doc = null;
        int i = 0;
        while (i < 3) {
            try {
                int waitingTime = getRandomTime();
                logService.LogMessage("Waiting: " + waitingTime / 1000 + " sec.");
                Thread.sleep(waitingTime);
                Proxy proxy = proxyService.getNewProxyAddress();
                logService.LogMessage("Used proxy: " + proxy.address());
                String userAgent = userAgentsRotatorService.getRandomUserAgent();
                logService.LogMessage("Used UserAgent: " + userAgent);
                if (!StringUtils.isEmpty(inputPlaceHolder)) {
                    logService.LogMessage(inputPlaceHolder);
                    Connection.Response response = Jsoup.connect(inputPlaceHolder)
                            .followRedirects(true)
                            .proxy(proxy)
                            .userAgent(userAgent)
                            .method(Connection.Method.GET)
                            .ignoreHttpErrors(true)
                            .execute();
                    logService.LogMessage("Request returned: " + response.statusCode() + " status code.");
                    doc = response.parse().body();
                    if (response.statusCode() != 200){
                        continue;
                    }
                    break;
                }
            } catch (Exception e) {
                logService.LogMessage("Error while request executing.");
                logService.LogMessage(e.getMessage());
                logService.drawLine();
            }
            i++;
        }
        return doc;
    }
}
