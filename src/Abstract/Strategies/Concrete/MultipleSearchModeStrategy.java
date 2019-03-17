package Abstract.Strategies.Concrete;

import Abstract.Engines.WebUrlEngine;
import Abstract.Factories.Concrete.BusinessListResultsFactory;
import Abstract.Factories.Concrete.RegularResultsFactory;
import Abstract.Models.OutputModels.IOutputModel;
import Abstract.Models.SearchResultModels.BusinessListSearchResultItem;
import Abstract.Models.SearchResultModels.RegularSearchResultItem;
import Abstract.Strategies.Concrete.MultipleSearchResultsDataConvertStrategy.ConvertBusinessSearchWithGeoDataStrategy;
import Abstract.Strategies.Concrete.MultipleSearchResultsDataConvertStrategy.ConvertSearchResultsWithGeoDataStrategy;
import Abstract.Strategies.SearchModeStrategyBase;
import Abstract.Strategies.ISearchResultsConvertStrategy;
import Abstract.Models.InputModels.InputCsvModelItem;
import Abstract.Models.RequestData;
import Services.*;
import Utils.StrUtils;
import org.jsoup.nodes.Element;
import org.tinylog.Logger;

import java.util.List;

public class MultipleSearchModeStrategy extends SearchModeStrategyBase {

    private boolean isWorkFlag;
    public MultipleSearchModeStrategy() {
    }

    public void processData(DIResolver diResolver) {
        GuiService guiService = new GuiService();
        InputDataService inputDataService = diResolver.getInputDataService();
        PropertiesService propertiesService = diResolver.getPropertiesService();
        OutputDataService outputDataService = diResolver.getOutputDataService();
        WebUrlEngine webUrlEngine = new WebUrlEngine();

        List<InputCsvModelItem> inputCsvItems = inputDataService.getInputCsvModelItems();
        int index = propertiesService.getIndex();

        isWorkFlag = true;
        Logger.tag("SYSTEM").info("Continue from: " + index + " record");
        for (int i = index; i < inputCsvItems.size(); i++) {
            InputCsvModelItem inputCsvModelItem = inputCsvItems.get(i);

            guiService.updateCountItemsStatus(i, inputCsvItems.size());
            if (!isWorkFlag) {
                break;
            }
            propertiesService.saveIndex(i);
            String URL = StrUtils.createUrlForMultipleSearch(inputCsvModelItem, guiService.getSearchPlaceholderText());
            RequestData requestData = new RequestData(URL);
            Element body = webUrlEngine.getWebSourceData(requestData);
            if (body == null) {
                continue;
            }

            RegularResultsFactory regularResultsFactory = new RegularResultsFactory();
            List<RegularSearchResultItem> regularSearchResultItems = regularResultsFactory.processBody(body);
            List filteredRegularSearchResultItems = filterGoogleResultData(regularSearchResultItems);
            ISearchResultsConvertStrategy<RegularSearchResultItem, IOutputModel> regularConvertStrategy
                    = new ConvertSearchResultsWithGeoDataStrategy(diResolver, inputCsvModelItem.getColumnA(), inputCsvModelItem.getColumnC());

            BusinessListResultsFactory businessListResultsFactory = new BusinessListResultsFactory();
            List<BusinessListSearchResultItem> businessListSearchResultItems = businessListResultsFactory.processBody(body);
            List filteredListSearchResultItems = filterGoogleResultData(businessListSearchResultItems);
            ISearchResultsConvertStrategy<BusinessListSearchResultItem, IOutputModel> businessListConvertStrategy
                    = new ConvertBusinessSearchWithGeoDataStrategy(inputCsvModelItem.getColumnA(), inputCsvModelItem.getColumnC());

            List regularItems = regularConvertStrategy.convertResultData(filteredRegularSearchResultItems);
            List listItems = businessListConvertStrategy.convertResultData(filteredListSearchResultItems);

            outputDataService.saveResultCsvItems(regularItems);
            outputDataService.saveResultCsvItems(listItems);
        }
    }

    public void stopProcessing() {
        isWorkFlag = false;
    }
}
