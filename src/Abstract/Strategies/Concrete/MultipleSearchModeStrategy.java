package Abstract.Strategies.Concrete;

import Abstract.Engines.WebUrlEngine;
import Abstract.Factories.Concrete.RegularResultsProcessing.RegularResultsFactory;
import Abstract.OutputModels.IOutputModel;
import Abstract.SearchResultModels.GoogleSearchResultItem;
import Abstract.SearchResultModels.RegularSearchResultItem;
import Abstract.Strategies.ISearchModeStrategy;
import Abstract.Strategies.ISearchResultsConvertStrategy;
import Models.InputCsvModelItem;
import Models.RequestData;
import Services.*;
import Utils.StrUtils;
import org.jsoup.nodes.Element;
import org.tinylog.Logger;
import java.util.List;

public class MultipleSearchModeStrategy implements ISearchModeStrategy {
    private boolean isWorkFlag;

    @Override
    public void processData() {
        GuiService guiService = new GuiService();
        InputDataService inputDataService = new InputDataService();
        PropertiesService propertiesService = new PropertiesService();

        inputDataService.initCSVItems(inputDataService.getInputDataFile());
        List<InputCsvModelItem> inputCsvItems = inputDataService.getInputCsvModelItems();
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
            RequestData requestData = new RequestData(URL, userAgentsRotatorService.getRandomUserAgent());
            Element body = webUrlEngine.getWebSourceData(requestData);
            if (body == null) {
                continue;
            }

            RegularResultsFactory regularResultsFactory = new RegularResultsFactory();
            //TODO: Business list implementation there:
            List<RegularSearchResultItem> regularSearchResultItems = regularResultsFactory.processBody(body);
            List filteredRegularSearchResultItems = searchService.filterGoogleResultData(regularSearchResultItems);

            ISearchResultsConvertStrategy<GoogleSearchResultItem, IOutputModel> convertStrategy
                    = new ConvertSearchResultsWithGeoDataStrategy(inputCsvModelItem.getColumnA(), inputCsvModelItem.getColumnC());

            List items = convertStrategy.convertResultData(filteredRegularSearchResultItems);
            outputDataService.saveResultCsvItems(items);
        }
    }

    @Override
    public void stopProcessing() {
        isWorkFlag = false;
    }
}
