package Abstract.Tasks;

import Abstract.Engines.CustomProxyMapsClient;
import Abstract.Engines.ProxyWebClient;
import Abstract.Models.OutputModels.IOutputModel;
import Abstract.Models.RequestData;
import Abstract.Models.SearchResultModels.GoogleSearchResultItem;
import Abstract.Models.SearchResultModels.RegularSearchResultItem;
import Abstract.Specifications.AbstractSpecification;
import Abstract.Strategies.EngineResultsInterpreters.RegularResultsItemsProcess;
import Abstract.Strategies.OutputResultsConversionStrategies.ConvertSearchResultsWithGeoDataStrategy;
import Abstract.Strategies.OutputResultsConversionStrategies.SearchResultsConvertStrategy;
import Services.DIResolver;
import Utils.ResultsUtils;
import kbaa.gsearch.PlaceCard;
import org.jsoup.nodes.Element;
import org.tinylog.Logger;
import java.io.IOException;
import java.util.List;

public class Worker implements Runnable {

    private final RequestData requestData;
    private final DIResolver diResolver;
    private final AbstractSpecification<GoogleSearchResultItem> googleItemsSpec;
    private final ProxyWebClient proxyWebClient;
    private final CustomProxyMapsClient customProxyMapsClient;

    public Worker(DIResolver diResolver, RequestData requestData, AbstractSpecification specification) {
        this.proxyWebClient = new ProxyWebClient();
        this.customProxyMapsClient = new CustomProxyMapsClient();
        this.diResolver = diResolver;
        this.requestData = requestData;
        googleItemsSpec = specification;
    }

    @Override
    public void run() {
        if (diResolver.getDbConnectionService().getWorkStatus()) {
            Element body = null;
            try {
                body = proxyWebClient.requestToSearchEngine(requestData, diResolver);
            } catch (IOException e) {
                Logger.tag("SYSTEM").error(e);
                Logger.error(e.getMessage());
            }

            List<PlaceCard> mapsItems = null;
            try {
                mapsItems = customProxyMapsClient.requestToMapsEngine(requestData, diResolver);
            } catch (IOException e) {
                Logger.tag("SYSTEM").error(e);
                Logger.error(e.getMessage());
            }

            RegularResultsItemsProcess regularResultsFactory = new RegularResultsItemsProcess();
            List<RegularSearchResultItem> regularSearchResultItems = regularResultsFactory.translateBodyToModels(body);
            List filteredRegularSearchResultItems = ResultsUtils.filterResults(regularSearchResultItems, googleItemsSpec);
            SearchResultsConvertStrategy<RegularSearchResultItem, IOutputModel> regularConvertStrategy
                    = new ConvertSearchResultsWithGeoDataStrategy(diResolver, requestData.inputCsvModelItem.getColumnA(), requestData.inputCsvModelItem.getColumnC());
            List regularItems = regularConvertStrategy.convertResultDataToOutputModels(filteredRegularSearchResultItems);
            List scrapedMapsItems = regularConvertStrategy.convertMapsResultDataToOutputModels(mapsItems);
            regularItems.addAll(scrapedMapsItems);

            diResolver.getOutputDataService().saveResultCsvItemsByMultipleSearch(regularItems);
        }
    }
}
