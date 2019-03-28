package Abstract.Strategies.SearchingModeStrategies;

import Abstract.Engines.ProxyWebClient;
import Abstract.Factories.EngineResultsInterpretersFactory.BusinessListResultsFactory;
import Abstract.Factories.EngineResultsInterpretersFactory.RegularResultsFactory;
import Abstract.Models.OutputModels.IOutputModel;
import Abstract.Models.RequestData;
import Abstract.Models.SearchResultModels.BusinessListSearchResultItem;
import Abstract.Models.SearchResultModels.RegularSearchResultItem;
import Abstract.Strategies.OutputResultsConversionStrategies.SingleSearchResultsDataConvertStrategy.ConvertBusinessSearchDataStrategy;
import Abstract.Strategies.OutputResultsConversionStrategies.SingleSearchResultsDataConvertStrategy.ConvertSearchResultsDataStrategy;
import Abstract.Strategies.OutputResultsConversionStrategies.SearchResultsConvertStrategy;
import Abstract.Strategies.SearchModeStrategyBase;
import Services.DIResolver;
import Services.GuiService;
import Services.OutputDataService;
import Utils.StrUtils;
import org.apache.commons.lang.StringUtils;
import org.jsoup.nodes.Element;
import org.tinylog.Logger;

import java.util.List;

public class SingleSearchModeStrategy extends SearchModeStrategyBase {

    public void processData(DIResolver diResolver) {
        OutputDataService outputDataService = diResolver.getOutputDataService();
        ProxyWebClient proxyWebClient = new ProxyWebClient(diResolver);
        GuiService guiService = diResolver.getGuiService();

        if (StringUtils.isEmpty(guiService.getSearchPlaceholderText())) {
            Logger.tag("SYSTEM").error("Placeholder term not specified");
            return;
        }

        String URL = StrUtils.createUrlForSingleSearch(guiService.getSearchPlaceholderText());
        RequestData requestData = new RequestData(URL, 10, 10000);
        Element body = proxyWebClient.request(requestData);

        if (body == null) {
            return;
        }
        RegularResultsFactory regularResultsFactory = new RegularResultsFactory();
        List<RegularSearchResultItem> regularSearchResultItems = regularResultsFactory.processBody(body);
        List filteredRegularSearchResultItems = filterGoogleResultData(regularSearchResultItems);
        SearchResultsConvertStrategy<RegularSearchResultItem, IOutputModel> regularConvertStrategy
                = new ConvertSearchResultsDataStrategy(diResolver);
        List regularItems = regularConvertStrategy.convertResultData(filteredRegularSearchResultItems);

        BusinessListResultsFactory businessListResultsFactory = new BusinessListResultsFactory();
        List<BusinessListSearchResultItem> businessListSearchResultItems = businessListResultsFactory.processBody(body);
        List filteredListSearchResultItems = filterGoogleResultData(businessListSearchResultItems);
        SearchResultsConvertStrategy<BusinessListSearchResultItem, IOutputModel> businessListConvertStrategy
                = new ConvertBusinessSearchDataStrategy();
        List listItems = businessListConvertStrategy.convertResultData(filteredListSearchResultItems);

        outputDataService.saveResultCsvItems(regularItems);
        outputDataService.saveResultCsvItems(listItems);
    }

    public void stopProcessing() {
    }
}
