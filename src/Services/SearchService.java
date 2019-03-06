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
import org.pmw.tinylog.Logger;

public class SearchService {

//    private final FileService fileService;
//    private final GuiService guiService;
//    private final LogService logService;
//    private final PropertiesService propertiesService;
//    private final ProxyService proxyService;
//    private final UserAgentsRotatorService userAgentsRotatorService;

    private ArrayList<InputCsvModelItem> inputCsvItems;
    private SearchSettings searchExceptions;
    private boolean isWorkFlag = false;

    public SearchService() {
//        this.fileService = DIResolver.getFileService();
//        this.logService =  DIResolver.getLogService();
//        this.guiService = DIResolver.getGuiService();
//        this.propertiesService = DIResolver.getPropertiesService();
//        this.proxyService = DIResolver.getProxyService();
//        this.userAgentsRotatorService = DIResolver.getUserAgentsRotatorService();
    }

    private int getRandomTime() {
        Random r = new Random();
        int low = 10000;
        int high = 15000;
        return r.nextInt(high-low) + low;
    }

//    public void DoWork(ArrayList<InputCsvModelItem> inputCsvItems, SearchSettings searchExceptions) {
//        this.inputCsvItems = inputCsvItems;
//        this.searchExceptions = searchExceptions;
//        Thread worker = new Thread(() -> {
//            guiService.changeApplicationStateToWork(true);
//            isWorkFlag = true;
//            StartWork();
//            guiService.changeApplicationStateToWork(false);
//            logService.LogMessage("Stopped");
//            guiService.setStatusText("Stopping...");
//        });
//        worker.start();
//    }

    public void StartWork(int startIndex, String placeHolderText) {
        try {
            int index = startIndex;
            if (StringUtils.isEmpty(placeHolderText)){
                Logger.error("Placeholder text empty");
                return;
            }
            if (searchExceptions == null){
                Logger.error("Exceptions file was not chosen");
                return;
            }
            if (inputCsvItems == null && StrUtils.isPlaceholderHasSubstituteTerms(placeHolderText)) {
                Element body = getQueryBody(null);
                if (body != null) {
                    SearchResult result = new SearchResult(logService)
                            .initSearchExceptions(searchExceptions)
                            .parsePageBody(body);
                    ArrayList<OutputCsvModelItem> items = fileService.mapSearchResultsToOutputCSVModels(result);
                    fileService.SaveResultCsvItems(items);
                }
            } else {
                Logger.info("Continue from: " + index + " record");
                for (int i = index; i < inputCsvItems.size(); i++) {
                    guiService.updateCountItemsStatus(i, inputCsvItems.size());
                    if (!isWorkFlag) {
                        break;
                    }
                    propertiesService.saveIndex(i);
                    Element body = getQueryBody(inputCsvItems.get(i));
                    if (body != null) {
                        SearchResult result = new SearchResult(logService)
                                .initCity(StrUtils.getSearchValue(inputCsvItems.get(i), placeHolderText))
                                .initCountry(inputCsvItems.get(i).getColumnB())
                                .initSearchExceptions(searchExceptions)
                                .parsePageBody(body);
                        ArrayList<OutputCsvModelItem> items = fileService.mapSearchResultsToOutputCSVModels(result);
                        fileService.SaveResultCsvItems(items);
                    }
                }
            }
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
    public void setWorkFlagToRun() {
        isWorkFlag = true;
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
                Logger.info("Waiting: " + waitingTime / 1000 + " sec.");
                Thread.sleep(waitingTime);
                Proxy proxy = proxyService.getNewProxyAddress();
                Logger.info("Used proxy: " + proxy.address());
                String userAgent = userAgentsRotatorService.getRandomUserAgent();
                Logger.info("Used UserAgent: " + userAgent);
                if (!StringUtils.isEmpty(inputPlaceHolder)) {
                    Logger.info(inputPlaceHolder);
                    Connection.Response response = Jsoup.connect(inputPlaceHolder)
                            .followRedirects(true)
                            .proxy(proxy)
                            .userAgent(userAgent)
                            .method(Connection.Method.GET)
                            .ignoreHttpErrors(true)
                            .execute();
                    Logger.info("Request returned: " + response.statusCode() + " status code.");
                    doc = response.parse().body();
                    if (response.statusCode() != 200){
                        continue;
                    }
                    break;
                }
            } catch (Exception e) {
                Logger.error(e, "Error while request executing.");
            }
            i++;
        }
        return doc;
    }
}
