package Services;

import Models.InputCsvModelItem;
import Models.OutputCsvModelItem;
import Models.ProxyObjects.ProxyObjectDto;
import Models.SearchExceptions;
import Models.SearchResult;
import Utils.StrUtils;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import org.jsoup.nodes.Element;

public class SearchService {

    private FileService fileService;
    private GuiService guiService;
    private LogService logService;
    private PropertiesService propertiesService;
    private ProxyService proxyService;
    private UserAgentsRotatorService userAgentsRotatorService;
    private Thread worker;

    boolean isWorkFlag = false;
    boolean isError = false;

    public SearchService(FileService fileService, LogService logService, GuiService guiService, PropertiesService propertiesService, ProxyService proxyService, UserAgentsRotatorService userAgentsRotatorService) {
        this.fileService = fileService;
        this.logService = logService;
        this.guiService = guiService;
        this.propertiesService = propertiesService;
        this.proxyService = proxyService;
        this.userAgentsRotatorService = userAgentsRotatorService;

        userAgentsRotatorService.initList();
        fileService.RestoreFilesControl();
        guiService.RestorePlaceholder();

        if (propertiesService.getWorkState()) {
            Work();
        }
    }

    private int getRandomTime() {
        Random r = new Random();
        int low = 15000;
        int high = 30000;
        return r.nextInt(high-low) + low;
    }

    public void Work() {
        worker = new Thread(() -> {
            guiService.changeApplicationStateToWork(true);
            isError = false;
            isWorkFlag = true;
            StartWork();
            guiService.changeApplicationStateToWork(false);
        });
        worker.start();
    }

    private void StartWork() {
        try {
            SearchExceptions se = fileService.initExceptionsKeywords();
            fileService.setUpOutputFile(guiService.getBootstrapper().getSearchingPlaceHolder().getText());
            int index = propertiesService.getIndex();
            logService.LogMessage("Continue from: " + index + " record");
            ArrayList<InputCsvModelItem> csvItems = fileService.InitCSVItems();
            for (int i = index; i < csvItems.size(); i++) {
                logService.updateCountItemsStatus(i, csvItems.size());
                if (!isWorkFlag) {
                    break;
                }
                propertiesService.saveIndex(i);
                Element body = getQueryBody(csvItems.get(i));
                if (body != null) {
                    SearchResult result = new SearchResult(logService)
                            .initCity(StrUtils.getSearchValue(csvItems.get(i), guiService.getSearchPlaceholderText()))
                            .initCountry(csvItems.get(i).getColumnB())
                            .initSearchExceptions(se)
                            .parsePageBody(body);
                    ArrayList<OutputCsvModelItem> items = fileService.mapSearchResultsToOutputCSVModels(result);
                    fileService.saveCSVItems(items);
                }
            }
        } catch (Exception e) {
            isWorkFlag = false;
            isError = true;
            guiService.changeApplicationStateToWork(false);
            logService.LogMessage("Application failed");
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
        ProxyObjectDto proxy = proxyService.getNewProxy();
        Element doc = null;
        try {
            Thread.sleep(getRandomTime());
            System.setProperty("http.proxyHost", proxy.ip);
            System.setProperty("http.proxyPort", String.valueOf(proxy.port));
            logService.LogMessage("Used proxy: " +proxy.ip + ":" + proxy.port);
            String userAgent = userAgentsRotatorService.getRandomUserAgent();
            logService.LogMessage("Used UserAgent: " + userAgent);
            if (!StringUtils.isEmpty(inputPlaceHolder)) {
                logService.LogMessage(inputPlaceHolder);
                Connection.Response response = Jsoup.connect(inputPlaceHolder)
                        .followRedirects(true)
                        .userAgent(userAgent)
                        .method(Connection.Method.GET)
                        .execute();

                System.clearProperty("http.proxyHost");
                System.clearProperty("http.proxyPort");
                if (response.statusCode() > 300) {
                    System.out.println("Request banned");
                }
                doc = response.parse().body();
            }
        } catch (IOException e) {
            logService.LogMessage("Error while request executing.");
        } catch (InterruptedException e) {
            logService.LogMessage("Error while request executing.");
        }
        return doc;
    }
}
