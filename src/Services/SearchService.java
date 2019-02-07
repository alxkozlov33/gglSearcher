package Services;

import Models.InputCsvModelItem;
import Models.OutputCsvModelItem;
import Models.SearchResult;
import Models.SearchResultItem;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrSubstitutor;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
            if(isRequestBanned(body)) {
                continue;
            }
            SearchResult result = new SearchResult(body, logService);
            fileService.saveCSVItems(mapSearchResultsToOutputCSV(result));
        }
    }

    private boolean isRequestBanned(Element body) {
        if (body == null || body.text().toLowerCase().contains("The document has moved")){
            logService.LogMessage("Request banned...");
            return true;
        }else {
            return false;
        }
    }

    private String createURL(InputCsvModelItem csvItem, String inputPlaceHolder) {
        String result = null;
        if (!StringUtils.isEmpty(inputPlaceHolder)){
            Map valuesMap = new HashMap();
            valuesMap.put("columnA", csvItem.getColumnA());
            valuesMap.put("columnB", csvItem.getColumnB());
            valuesMap.put("columnC", csvItem.getColumnC());
            valuesMap.put("columnD", csvItem.getColumnD());
            valuesMap.put("columnE", csvItem.getColumnE());
            valuesMap.put("columnF", csvItem.getColumnF());
            valuesMap.put("columnG", csvItem.getColumnG());
            valuesMap.put("columnH", csvItem.getColumnH());
            valuesMap.put("columnI", csvItem.getColumnI());
            valuesMap.put("columnJ", csvItem.getColumnJ());
            valuesMap.put("columnK", csvItem.getColumnK());
            valuesMap.put("columnL", csvItem.getColumnL());
            valuesMap.put("columnM", csvItem.getColumnM());
            valuesMap.put("columnN", csvItem.getColumnN());
            valuesMap.put("columnO", csvItem.getColumnO());
            StrSubstitutor sub = new StrSubstitutor(valuesMap);

            if (StringUtils.isEmpty(inputPlaceHolder)) {
                logService.LogMessage("Define search placeholder");
                isWorkFlag = false;
            }
            else {
                try {
                    result = "https://duckduckgo.com/html/?q=" + URLEncoder.encode(sub.replace(inputPlaceHolder), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    public void setWorkStateToStop() {
        isWorkFlag = false;
    }

    private Connection.Response executeRequest(InputCsvModelItem item, int timeout) {
        if (!isWorkFlag) {
            return null;
        }
        Connection.Response response = null;
        try {
            String string = createURL(item, guiService.getBootstrapper().getSearchingPlaceHolder().getText());
            Thread.sleep(500);
            if (!StringUtils.isEmpty(string)) {
                logService.LogMessage(string);
                response = Jsoup.connect(string)
                        .followRedirects(true)
                        .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_1) AppleWebKit/602.2.14 (KHTML, like Gecko) Version/10.0.1 Safari/602.2.14")
                        .method(Connection.Method.GET)
                        .execute();
            }
        } catch (IOException e) {
            e.printStackTrace();
            logService.LogMessage("Cannot start searching");
        }
        catch (InterruptedException e) {
            logService.LogMessage("Error while request executing.");
        }
        return response;
    }

       private Element getQueryBody(InputCsvModelItem item) {
        Element doc = null;
        try {
            Connection.Response response = executeRequest(item, 2000);

//            if (response.statusCode() == 302 || response.statusCode() > 500) {
//                System.out.println("Request banned");
//            }
            if (response != null) {
                doc = response.parse().body();
            }
        } catch (Exception e) {
            logService.LogMessage("Error while query parsing");
        }
        return doc;
    }

    private ArrayList<OutputCsvModelItem> mapSearchResultsToOutputCSV(SearchResult results) {
        ArrayList<OutputCsvModelItem> outputItems = new ArrayList<>();
        if (results.getResults().size() == 0) {
            return null;
        }
        for (SearchResultItem item : results.getResults()) {
            outputItems.add(new OutputCsvModelItem(item.getGalleryName(), item.getWebsite(), item.getAddress()));
        }
        return outputItems;
    }
}
