package Services;

import Models.InputCsvModelItem;
import Models.SearchResult;
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
import java.util.Random;

import org.jsoup.nodes.Element;

public class SearchService {

    private FileService fileService;
    private GuiService guiService;
    private LogService logService;
    private PropertiesService propertiesService;
    private Thread worker;

    int max = 70000;
    int min = 30000;

    boolean isWorkFlag = false;
    boolean isError = false;

    public SearchService(FileService fileService, LogService logService, GuiService guiService, PropertiesService propertiesService) {
        this.fileService = fileService;
        this.logService = logService;
        this.guiService = guiService;
        this.propertiesService = propertiesService;

        if (propertiesService.getWorkState()) {
            fileService.RestoreFilesControl();
            guiService.RestorePlaceholder();
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
            if (!isWorkFlag) {
                break;
            }
            propertiesService.saveIndex(i);
            Element body = getQueryBody(csvItems.get(i));
            if(isRequestBanned(body)) {
                continue;
            }
            SearchResult result = new SearchResult(body);
            logService.LogMessage("Found: "+result.getResults().size()+" results. Parsing.");
            checkResultToInstagramLink(result, csvItems.get(i)); //TODO: log info about found results
            //fileService.saveCSVItems(); //TODO: save items
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
            valuesMap.put("city", csvItem.getCity());
            StrSubstitutor sub = new StrSubstitutor(valuesMap);
            try {
                result = "https://www.google.com/search?q=" + URLEncoder.encode(sub.replace(inputPlaceHolder), "UTF-8") + "&pws=0&gl=us&gws_rd=cr";
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
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
            if (timeout > (max + min)) {
                logService.UpdateStatus("Waiting: " + (timeout/1000)/60 + " min");
                logService.LogMessage("Waiting: " + (timeout/1000)/60 + " min");
            }else {
                logService.UpdateStatus("Waiting: " + (timeout/1000) + " sec");
                logService.LogMessage("Waiting: " + (timeout/1000) + " sec");
            }

            Thread.sleep(timeout);
            String string = createURL(item, guiService.getBootstrapper().getSearchingPlaceHolder().getText());
            if (!StringUtils.isEmpty(string)) {
                logService.LogMessage(string);
                response = Jsoup.connect(string)
                        .followRedirects(false)
                        .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_1) AppleWebKit/602.2.14 (KHTML, like Gecko) Version/10.0.1 Safari/602.2.14")
                        .method(Connection.Method.GET)
                        .execute();
            }
        } catch (IOException e) {
            e.printStackTrace();
            logService.LogMessage("Cannot start searching");
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        return response;
    }

    private Element getQueryBody(InputCsvModelItem item) {
        Element doc = null;
        try {
            Connection.Response response = executeRequest(item, min + new Random().nextInt(max));
            if (response.statusCode() == 302) {
                int triesCounter = 1;
                while (triesCounter < 3) {
                    response = executeRequest(item, (min +(1200000 * triesCounter)) + new Random().nextInt(max + (1200000 * triesCounter)));
                    triesCounter++;
                }
            }
            if (response != null) {
                doc = response.parse().body();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return doc;
    }

    private void checkResultToInstagramLink(SearchResult results, InputCsvModelItem csvItem) {
        for (int i = 0; i < results.getResults().size(); i++){

        }
    }
}
