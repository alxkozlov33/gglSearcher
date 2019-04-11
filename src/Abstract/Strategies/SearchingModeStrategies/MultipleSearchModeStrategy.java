package Abstract.Strategies.SearchingModeStrategies;

import Abstract.Engines.ProxyWebClient;
import Abstract.Engines.ProxyWebEngine;
import Abstract.Models.OutputModels.IOutputModel;
import Abstract.Models.SearchResultModels.BusinessListSearchResultItem;
import Abstract.Models.SearchResultModels.RegularSearchResultItem;
import Abstract.Strategies.EngineResultsInterpreters.BusinessListResultsProcessing.BusinessResultItemsProcess;
import Abstract.Strategies.EngineResultsInterpreters.RegularResultsProcessing.RegularResultsItemsProcess;
import Abstract.Strategies.OutputResultsConversionStrategies.MultipleSearchResultsDataConvertStrategy.ConvertBusinessSearchWithGeoDataStrategy;
import Abstract.Strategies.OutputResultsConversionStrategies.MultipleSearchResultsDataConvertStrategy.ConvertSearchResultsWithGeoDataStrategy;
import Abstract.Strategies.SearchModeStrategyBase;
import Abstract.Strategies.OutputResultsConversionStrategies.SearchResultsConvertStrategy;
import Abstract.Models.InputModels.InputCsvModelItem;
import Abstract.Models.RequestData;
import Services.*;
import Utils.StrUtils;
import org.jsoup.Jsoup;
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
            RequestData requestData = new RequestData(URL, 5, 3000);
            Element body = new ProxyWebClient().request(requestData);

            try {
//                RegularResultsItemsProcess regularResultsFactory = new RegularResultsItemsProcess();
//                List<RegularSearchResultItem> regularSearchResultItems = regularResultsFactory.translateBodyToModels(body);
//                List filteredRegularSearchResultItems = filterGoogleResultData(regularSearchResultItems);
//                SearchResultsConvertStrategy<RegularSearchResultItem, IOutputModel> regularConvertStrategy
//                        = new ConvertSearchResultsWithGeoDataStrategy(diResolver, inputCsvModelItem.getColumnA(), inputCsvModelItem.getColumnC());
//                List regularItems = regularConvertStrategy.convertResultDataToOutputModels(filteredRegularSearchResultItems);


                //TODO: Debug.
                List<BusinessListSearchResultItem> businessListSearchResultItems = new BusinessResultItemsProcess().processData(body, inputCsvModelItem);
                List filteredListSearchResultItems = filterGoogleResultData(businessListSearchResultItems);
                SearchResultsConvertStrategy<BusinessListSearchResultItem, IOutputModel> businessListConvertStrategy
                        = new ConvertBusinessSearchWithGeoDataStrategy(inputCsvModelItem.getColumnA(), inputCsvModelItem.getColumnC());

                List listItems = businessListConvertStrategy.convertResultDataToOutputModels(filteredListSearchResultItems);

                //outputDataService.saveResultCsvItemsByMultipleSearch(regularItems);
                outputDataService.saveResultCsvItemsByMultipleSearch(listItems);
            }
            catch (Exception ex) {
                Logger.tag("SYSTEM").error(ex);
            }
        }
    }

    public void stopProcessing() {
        isWorkFlag = false;
    }
}
