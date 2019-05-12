package Abstract.Strategies.SearchingModeStrategies;

import Abstract.Engines.ProxyWebClient;
import Abstract.Models.OutputModels.IOutputModel;
import Abstract.Models.RequestData;
import Abstract.Models.SearchResultModels.RegularSearchResultItem;
import Abstract.Strategies.EngineResultsInterpreters.RegularResultsItemsProcess;
import Abstract.Strategies.OutputResultsConversionStrategies.SearchResultsConvertStrategy;
import Abstract.Strategies.OutputResultsConversionStrategies.ConvertSearchResultsDataStrategy;
import Abstract.Strategies.SearchModeStrategyBase;
import Services.DIResolver;
import Services.GuiService;
import Services.OutputDataService;
import Utils.ResultsUtils;
import Utils.StrUtils;
import org.apache.commons.lang.StringUtils;
import org.jsoup.nodes.Element;
import org.tinylog.Logger;
import java.io.IOException;
import java.util.List;

public class SingleSearchModeStrategy extends SearchModeStrategyBase {

    private final DIResolver diResolver;
    public SingleSearchModeStrategy(DIResolver diResolver) {
        this.diResolver = diResolver;
    }

    @Override
    public void processData() {
        OutputDataService outputDataService = diResolver.getOutputDataService();
        GuiService guiService = diResolver.getGuiService();

        if (StringUtils.isEmpty(guiService.getSearchPlaceholderText())) {
            Logger.tag("SYSTEM").error("Placeholder term not specified");
            return;
        }

        String URL = StrUtils.createUrlForSingleSearch(guiService.getSearchPlaceholderText());
        RequestData requestData = new RequestData(URL, 10, 10000);
        ProxyWebClient webClient = new ProxyWebClient();
        Element body = null;
        try {
            body = webClient.request(requestData);
        } catch (IOException e) {
            Logger.tag("SYSTEM").error(e);
        }

        RegularResultsItemsProcess regularResultsFactory = new RegularResultsItemsProcess();
        List<RegularSearchResultItem> regularSearchResultItems = regularResultsFactory.translateBodyToModels(body);
        List filteredRegularSearchResultItems = ResultsUtils.filterResults(regularSearchResultItems, getSettingsSpecification(diResolver));
        SearchResultsConvertStrategy<RegularSearchResultItem, IOutputModel> regularConvertStrategy
                = new ConvertSearchResultsDataStrategy(diResolver);
        List regularItems = regularConvertStrategy.convertResultDataToOutputModels(filteredRegularSearchResultItems);

        outputDataService.saveResultCsvItems(regularItems);
    }

    public void stopProcessing() {
    }
}
