package Services;

import Models.InputCsvModelItem;
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
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.nodes.Element;

public class SearchService {

    private FileService fileService;
    private GuiService guiService;
    private LogService logService;
    private PropertiesService propertiesService;
    private Thread worker;

    int max = 8000;
    int min = 5000;

    boolean isWorkFlag = false;
    boolean isError = false;

    public SearchService(FileService fileService, LogService logService, GuiService guiService, PropertiesService propertiesService) {
        this.fileService = fileService;
        this.logService = logService;
        this.guiService = guiService;
        this.propertiesService = propertiesService;
    }

    public void Work() {
        worker = new Thread(() -> {
            guiService.changeApplicationStateToWork(true);
            isError = false;
            StartWork();
            guiService.changeApplicationStateToWork(false);
        });
        worker.start();
    }

    private void StartWork () {
        int index = propertiesService.getIndex();
        isWorkFlag = true;
        ArrayList<InputCsvModelItem> csvItems = fileService.InitCSVItems();
        for (int i = index; i < csvItems.size();  i++) {
            if (!isWorkFlag) {
                break;
            }
            propertiesService.saveIndex(i);
            Element body = getQueryBody(csvItems.get(i));
            if (body == null) {
                continue;
            }
            SearchResult result = new SearchResult(body);
            logService.LogMessage("Found: "+result.getResults().size()+"... Parsing.");
            checkResultToInstagramLink(result, csvItems.get(i));
            //fileService.saveCSVItems(); //TODO: save items
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

    private Connection.Response executeRequest(InputCsvModelItem item, int timeout) {
        if (!isWorkFlag) {
            return null;
        }
        Connection.Response response = null;
        try {
            System.out.println("Processing: " + timeout/1000 + " sec");
            if (timeout > (max + min)) {
                logService.UpdateStatus("Waiting: " + (timeout/1000)/60 + " min");
            }else {
                logService.UpdateStatus("Waiting: " + (timeout/1000)/60 + " min");
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
