package Abstract.Strategies.EngineResultsInterpreters.BusinessListResultsProcessing;
import Abstract.Engines.ProxyWebClient;
import Abstract.Models.RequestData;
import Abstract.Models.SearchResultModels.BusinessListSearchResultItem;
import Services.DIResolver;
import Utils.StrUtils;
import org.jsoup.nodes.Element;
import org.tinylog.Logger;

class AdditionalBusinessRequestToGoogle {
    private final ProxyWebClient proxyWebClient;

    AdditionalBusinessRequestToGoogle(DIResolver diResolver) {
        this.proxyWebClient = new ProxyWebClient(diResolver);
    }

    synchronized void processData(BusinessListSearchResultItem businessListSearchResultItem) {
        Element element = proxyWebClient.request(new RequestData(StrUtils.createUrlForAdditionalPlacesSearch(businessListSearchResultItem), 5, 10000));
        Element webSite = null;
        try {
            webSite = element.selectFirst("#rhs_block > div > div:eq(1) > div > div:eq(1) > div:eq(2) > div:eq(2) > div > div:eq(2) > div:eq(1) > div > div > div:eq(2) > div:eq(1) > a");
        } catch(Exception ex) {
            Logger.error("Cannot locate details");
        }

        if (webSite == null) {
            return;
        }
        businessListSearchResultItem.setLink(webSite.attr("href"));
    }
}
