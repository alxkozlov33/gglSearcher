package Services;

import Models.InputCsvModelItem;
import Models.OutputCsvModelItem;
import Models.SearchExceptions;
import Models.SearchResult;
import Utils.StrUtils;
import org.apache.commons.lang.StringUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Scanner;

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
        SearchExceptions se = fileService.initExceptions();
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
            String inputPlaceHolder = StrUtils.createURL(item, guiService.getSearchPlaceholderText());
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
                        .userAgent("DuckDuckBot/1.0; (+http://duckduckgo.com/duckduckbot.html)")
                        .method(Connection.Method.GET)
                        .execute();
            }
        } catch (IOException e) {
            logService.LogMessage("Error while request executing.");
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
