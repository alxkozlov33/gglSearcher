package Abstract.Strategies.Concrete;

import Abstract.Engines.WebUrlEngine;
import Abstract.Factories.Concrete.RegularResultsFactory;
import Abstract.SearchResultModels.RegularSearchResultItem;
import Abstract.Strategies.ISearchModeStrategy;
import Models.InputCsvModelItem;
import Models.RequestData;
import Services.*;
import Utils.StrUtils;
import org.jsoup.nodes.Element;
import org.tinylog.Logger;

import java.util.ArrayList;
import java.util.List;

public class MultipleSearchModeStrategy implements ISearchModeStrategy {
    private boolean isWorkFlag;

    @Override
    public void processData(GuiService guiService) {
        InputDataService inputDataService = new InputDataService();
        PropertiesService propertiesService = new PropertiesService();

        inputDataService.initCSVItems(inputDataService.getInputDataFile());
        List<InputCsvModelItem> inputCsvItems = inputDataService.getInputCsvModelItems();

        ProxyService proxyService = new ProxyService();
        UserAgentsRotatorService userAgentsRotatorService = new UserAgentsRotatorService();

        int index = propertiesService.getIndex();

        isWorkFlag = true;
        Logger.tag("SYSTEM").info("Continue from: " + index + " record");
        WebUrlEngine webUrlEngine = new WebUrlEngine();
        OutputDataService outputDataService = new OutputDataService();
        SearchService searchService = new SearchService();
        for (int i = index; i < inputCsvItems.size(); i++) {
            InputCsvModelItem inputCsvModelItem = inputCsvItems.get(i);

            guiService.updateCountItemsStatus(i, inputCsvItems.size());
            if (!isWorkFlag) {
                break;
            }
            propertiesService.saveIndex(i);

            String URL = StrUtils.createURL(inputCsvModelItem, guiService.getSearchPlaceholderText());
            RequestData requestData = new RequestData(URL, userAgentsRotatorService.getRandomUserAgent(), proxyService.getNewProxyAddress());

            Element body = webUrlEngine.getWebSourceData(requestData);
            RegularResultsFactory regularResultsFactory = new RegularResultsFactory();
            //TODO: Business list implementation there:
            List<RegularSearchResultItem> regularSearchResultItems = regularResultsFactory.processBody(body);

            List filteredRegularSearchResultItems = searchService.filterGoogleResultData(regularSearchResultItems);

            ArrayList items = outputDataService.mapSearchResultsToOutputCSVModels(filteredRegularSearchResultItems);
            outputDataService.saveResultCsvItems(items);
        }
    }

    @Override
    public void stopProcessing() {
        isWorkFlag = false;
    }
}
