package Abstract.Strategies.OutputResultsConversionStrategies;

import Abstract.Engines.ProxyWebClient;
import Abstract.Models.OutputModels.IOutputModel;
import Abstract.Models.RequestData;
import Abstract.Models.SearchResultModels.GoogleSearchResultItem;
import Abstract.Models.SearchResultModels.WebPageObject;
import Services.DIResolver;
import Utils.StrUtils;
import org.apache.commons.lang.StringUtils;
import org.jsoup.nodes.Element;

import java.util.List;


//TODO: Refactor
public abstract class SearchResultsConvertStrategy<T extends GoogleSearchResultItem, U extends IOutputModel> {

    public abstract List<U> convertResultData(List<T> searchItems);

    protected Element getWebSitePageSource(GoogleSearchResultItem item, DIResolver diResolver) {
        RequestData requestData = new RequestData(item.getLink(), 10, 10000);
        return new ProxyWebClient(diResolver).request(requestData);
    }

    protected WebPageObject parseSourceData(Element pageSourceData){
        if (pageSourceData == null) {
            return null;
        }
        String siteDescription = pageSourceData.select("meta[name=description]").attr("content");
        String siteKeywords = pageSourceData.select("meta[name=keywords]").attr("content");
        String siteName = pageSourceData.select("meta[property=og:title]").attr("content");
        if (StringUtils.isEmpty(siteName)) {
            siteName = pageSourceData.select("title").text();
        }
        return new WebPageObject(siteDescription, siteKeywords, siteName, pageSourceData.text());
    }

    protected String getHtmlPageTitle(WebPageObject webPageObject, GoogleSearchResultItem googleSearchResultItem) {
        if (StringUtils.isEmpty(webPageObject.getSiteName())) {
            return googleSearchResultItem.getMainHeader();
        }
        return webPageObject.getSiteName();
    }

    protected String getNotSureLink(GoogleSearchResultItem googleSearchResultItem) {
        String notSureLink = "";
        String urlPath = StrUtils.getUnmatchedPartOfString(googleSearchResultItem.getLink());
        if (urlPath.length() > 15){
            notSureLink = googleSearchResultItem.getLink();
        }
        return notSureLink;
    }

    protected String getWebSite(GoogleSearchResultItem googleSearchResultItem) {
        String webSite = "";
        String urlPath = StrUtils.getUnmatchedPartOfString(googleSearchResultItem.getLink());
        if (urlPath.length() < 15){
            webSite = StrUtils.extractDomainName(googleSearchResultItem.getLink());
        }
        return webSite;
    }
}
