package Abstract.Strategies.OutputResultsConversionStrategies;

import Abstract.Engines.ProxyWebClient;
import Abstract.Exceptions.InputFileEmptyException;
import Abstract.Models.OutputModels.IOutputModel;
import Abstract.Models.RequestData;
import Abstract.Models.SearchResultModels.GoogleSearchResultItem;
import Abstract.Models.SearchResultModels.WebPageObject;
import Utils.StrUtils;
import org.apache.commons.lang.StringUtils;
import org.jsoup.nodes.Element;
import org.tinylog.Logger;
import java.io.IOException;
import java.util.List;


//TODO: Refactor
public abstract class SearchResultsConvertStrategy<T extends GoogleSearchResultItem, U extends IOutputModel> {

    public abstract List<U> convertResultDataToOutputModels(List<T> searchItems);

    protected Element getWebSitePageSource(GoogleSearchResultItem item) {
        RequestData requestData = new RequestData(item.getLink(), 5, 2000);
        Element result = null;
        try {
            result = new ProxyWebClient().request(requestData);
        } catch (IOException e) {
            Logger.error(e);
        }
        return result;
    }

    WebPageObject parseSourceData(Element pageSourceData){
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

    String getHtmlPageTitle(WebPageObject webPageObject, GoogleSearchResultItem googleSearchResultItem) {
        if (StringUtils.isEmpty(webPageObject.getSiteName())) {
            return googleSearchResultItem.getMainHeader();
        }
        return webPageObject.getSiteName();
    }

    String getNotSureLink(GoogleSearchResultItem googleSearchResultItem) {
        String notSureLink = "";
        String urlPath = StrUtils.getUnmatchedPartOfString(googleSearchResultItem.getLink());
        if (urlPath.length() > 15){
            notSureLink = googleSearchResultItem.getLink();
        }
        return notSureLink;
    }

    String getWebSite(GoogleSearchResultItem googleSearchResultItem) {
        String webSite = "";
        String urlPath = StrUtils.getUnmatchedPartOfString(googleSearchResultItem.getLink());
        if (urlPath.length() < 15){
            webSite = StrUtils.extractDomainName(googleSearchResultItem.getLink());
        }
        return webSite;
    }
}
