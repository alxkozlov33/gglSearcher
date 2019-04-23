package Abstract.Tasks;

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
import org.jsoup.nodes.Element;
import org.tinylog.Logger;
import java.io.IOException;
import java.util.List;

public class Worker implements Runnable {

    private final RequestData requestData;
    private final DIResolver diResolver;
    private final AbstractSpecification<GoogleSearchResultItem> googleItemsSpec;
    private final ProxyWebClient proxyWebClient;

    public Worker(DIResolver diResolver, RequestData requestData, AbstractSpecification specification) {
        proxyWebClient = new ProxyWebClient();
        this.diResolver = diResolver;
        this.requestData = requestData;
        googleItemsSpec = specification;
    }

    @Override
    public void run() {
        if (diResolver.getPropertiesService().getWorkState()) {
            Element body = null;
            try {
                body = proxyWebClient.request(requestData);
            } catch (IOException e) {
                Logger.tag("SYSTEM").error(e);
            }
            RegularResultsItemsProcess regularResultsFactory = new RegularResultsItemsProcess();
            List<RegularSearchResultItem> regularSearchResultItems = regularResultsFactory.translateBodyToModels(body);
            List filteredRegularSearchResultItems = ResultsUtils.filterResults(regularSearchResultItems, googleItemsSpec);
            SearchResultsConvertStrategy<RegularSearchResultItem, IOutputModel> regularConvertStrategy
                    = new ConvertSearchResultsWithGeoDataStrategy(diResolver, requestData.inputCsvModelItem.getColumnA(), requestData.inputCsvModelItem.getColumnC());
            List regularItems = regularConvertStrategy.convertResultDataToOutputModels(filteredRegularSearchResultItems);

            diResolver.getOutputDataService().saveResultCsvItemsByMultipleSearch(regularItems);
        }
    }
}
