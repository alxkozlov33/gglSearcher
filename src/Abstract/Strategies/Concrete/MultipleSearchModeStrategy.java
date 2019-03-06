package Abstract.Strategies.Concrete;

import Abstract.Engines.WebUrlEngine;
import Abstract.Factories.Concrete.RegularResultsResultFactory;
import Abstract.SearchResultModels.RegularSearchResult;
import Abstract.Strategies.ISearchModeStrategy;
import Models.InputCsvModelItem;
import Abstract.OutputModels.OutputCsvModelItem;
import Models.RequestData;
import Services.FileService;
import Services.GuiService;
import Services.PropertiesService;
import Services.UserAgentsRotatorService;
import Services.ProxyService;
import Utils.StrUtils;
import org.jsoup.nodes.Element;
import org.pmw.tinylog.Logger;

import java.util.ArrayList;
import java.util.List;

public class MultipleSearchModeStrategy implements ISearchModeStrategy {
    boolean isWorkFlag;

    @Override
    public void processData(FileService fileService, PropertiesService propertiesService, GuiService guiService) {
        ArrayList<InputCsvModelItem> inputCsvItems = fileService.InitCSVItems();

        ProxyService proxyService = new ProxyService();
        UserAgentsRotatorService userAgentsRotatorService = new UserAgentsRotatorService();

        int index = propertiesService.getIndex();

        isWorkFlag = true;
        Logger.info("Continue from: " + index + " record");
        for (int i = index; i < inputCsvItems.size(); i++) {
            InputCsvModelItem inputCsvModelItem = inputCsvItems.get(i);

            guiService.updateCountItemsStatus(i, inputCsvItems.size());
            if (!isWorkFlag) {
                break;
            }
            propertiesService.saveIndex(i);

            String URL = StrUtils.createURL(inputCsvModelItem, guiService.getSearchPlaceholderText());
            RequestData requestData = new RequestData(URL, userAgentsRotatorService.getRandomUserAgent(), proxyService.getNewProxyAddress());

            WebUrlEngine webUrlEngine = new WebUrlEngine();
            Element body = webUrlEngine.getWebSourceData(requestData);

            RegularResultsResultFactory regularResultsFactory = new RegularResultsResultFactory();
            //TODO: Business list implementation there:
            List<RegularSearchResult> regularSearchResults = regularResultsFactory.processBody(body);

//            if (body != null) {
//                SearchResult result = new SearchResult()
//                        .initSearchExceptions(searchExceptions)
//                        .parsePageBody(body);
                ArrayList<OutputCsvModelItem> items = fileService.mapSearchResultsToOutputCSVModels(regularSearchResults);
                fileService.SaveResultCsvItems(items);
            }
        }
    }
}
