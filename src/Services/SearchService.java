package Services;

import Models.InputCsvModelItem;
import Models.OutputCsvModelItem;
import Models.SearchResult;
import Utils.StrUtils;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.nodes.Element;

public class SearchService {

    private FileService fileService;
    private GuiService guiService;
    private LogService logService;
    private PropertiesService propertiesService;
    private Thread worker;

    boolean isWorkFlag = false;
    boolean isError = false;

    public SearchService(FileService fileService, LogService logService, GuiService guiService, PropertiesService propertiesService) {
        this.fileService = fileService;
        this.logService = logService;
        this.guiService = guiService;
        this.propertiesService = propertiesService;

        fileService.RestoreFilesControl();
        guiService.RestorePlaceholder();
        if (propertiesService.getWorkState()) {
            Work();
        }
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

    private void StartWork () {
        fileService.setUpOutputFile(guiService.getBootstrapper().getSearchingPlaceHolder().getText());
        int index = propertiesService.getIndex();
        logService.LogMessage("Continue from: " + index + " record");
        ArrayList<InputCsvModelItem> csvItems = fileService.InitCSVItems();
        for (int i = index; i < csvItems.size();  i++) {
            logService.updateCountItemsStatus(i, csvItems.size());
            if (!isWorkFlag) {
                break;
            }
            propertiesService.saveIndex(i);
            Element body = getQueryBody(csvItems.get(i));
            SearchResult result = new SearchResult(body, logService);
            ArrayList<OutputCsvModelItem> items = fileService.mapSearchResultsToOutputCSVModels(result);
            fileService.saveCSVItems(items);
        }
    }

    public void setWorkStateToStop() {
        isWorkFlag = false;
    }

    private Connection.Response executeRequest(InputCsvModelItem item) {
        if (!isWorkFlag) {
            return null;
        }
        Connection.Response response = null;
        try {
            String inputPlaceHolder = StrUtils.createURL(item, guiService.getBootstrapper().getSearchingPlaceHolder().getText());
            if (StringUtils.isEmpty(inputPlaceHolder)) {
                logService.LogMessage("Define search placeholder");
                isWorkFlag = false;
                return null;
            }
            Thread.sleep(500);
            if (!StringUtils.isEmpty(inputPlaceHolder)) {
                logService.LogMessage(inputPlaceHolder);
                response = Jsoup.connect(inputPlaceHolder)
                        .followRedirects(true)
                        .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_1) AppleWebKit/602.2.14 (KHTML, like Gecko) Version/10.0.1 Safari/602.2.14")
                        .method(Connection.Method.GET)
                        .execute();
            }
        } catch (IOException e) {
            e.printStackTrace();
            logService.LogMessage("Cannot start searching");
        } catch (InterruptedException e) {
            logService.LogMessage("Error while request executing.");
        }
        return response;
    }

       private Element getQueryBody(InputCsvModelItem item) {
        Element doc = null;
        try {
            Connection.Response response = executeRequest(item);
            if (response != null) {
                if (response.statusCode() > 300) {
                    System.out.println("Request banned");
                }
                doc = response.parse().body();
            }
        } catch (Exception e) {
            logService.LogMessage("Error while query parsing");
        }
        return doc;
    }
}
