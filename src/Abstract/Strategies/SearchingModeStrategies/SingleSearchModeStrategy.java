package Abstract.Strategies.SearchingModeStrategies;

import Abstract.Engines.CustomProxyMapsClient;
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
import kbaa.gsearch.PlaceCard;
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
        if (!diResolver.getDbConnectionService().getGoogleSearchEngine() && !diResolver.getDbConnectionService().getGoogleMapsEngine()) {
            message = "No one search engine chosen";
            return;
        }

        String URL = StrUtils.createUrlForSingleSearch(guiService.getSearchPlaceholderText());
        RequestData requestData = new RequestData(URL, 10, 3000);
        requestData.setRequestTerm(StrUtils.encodeStringToUTF8(guiService.getSearchPlaceholderText()));

        Element body = null;
        if (diResolver.getDbConnectionService().getGoogleSearchEngine()) {
            body = getBodyOfSearchResults(requestData);
        }

        List<PlaceCard> mapsItems = null;
        if (diResolver.getDbConnectionService().getGoogleMapsEngine()) {
            mapsItems = getPlacesOfMapsResults(requestData);
        }

        if (body == null && mapsItems == null) {
            message = "Search finished with problems. Data wasn't scraped.";
            return;
        }

        RegularResultsItemsProcess regularResultsFactory = new RegularResultsItemsProcess();
        List<RegularSearchResultItem> regularSearchResultItems = regularResultsFactory.translateBodyToModels(body);
        List filteredRegularSearchResultItems = ResultsUtils.filterResults(regularSearchResultItems, getSettingsSpecification(diResolver));
        SearchResultsConvertStrategy<RegularSearchResultItem, IOutputModel> regularConvertStrategy
                = new ConvertSearchResultsDataStrategy(diResolver);
        List regularItems = regularConvertStrategy.convertResultDataToOutputModels(filteredRegularSearchResultItems);
        List scrapedMapsItems = regularConvertStrategy.convertMapsResultDataToOutputModels(mapsItems);

        regularItems.addAll(scrapedMapsItems);

        outputDataService.saveResultCsvItems(regularItems);
        message = "Finished";
    }

    public void stopProcessing() {
    }

    private List<PlaceCard> getPlacesOfMapsResults(RequestData requestData) {
        List<PlaceCard> mapsItems = null;
        try {
            CustomProxyMapsClient customProxyMapsClient = new CustomProxyMapsClient();
            mapsItems = customProxyMapsClient.requestToMapsEngine(requestData, diResolver);
        } catch (IOException e) {
            Logger.error(e);
            Logger.tag("SYSTEM").error(e.getMessage());
        }
        return mapsItems;
    }

    private Element getBodyOfSearchResults(RequestData requestData) {
        Element body = null;
        try {
            ProxyWebClient webClient = new ProxyWebClient();
            body = webClient.requestToSearchEngine(requestData, diResolver);
        } catch (IOException e) {
            Logger.error(e);
            Logger.tag("SYSTEM").error(e.getMessage());
        }
        return body;
    }
}
