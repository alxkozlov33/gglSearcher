package Abstract.Strategies.OutputResultsConversionStrategies.SingleSearchResultsDataConvertStrategy;

import Abstract.Engines.WebUrlEngine;
import Abstract.Models.OutputModels.IOutputModel;
import Abstract.Models.OutputModels.OutputRegularCSVItem;
import Abstract.Models.RequestData;
import Abstract.Models.SearchResultModels.GoogleSearchResultItem;
import Abstract.Models.SearchResultModels.RegularSearchResultItem;
import Abstract.Models.SearchResultModels.WebPageObject;
import Abstract.Specifications.Concrete.MetaTagsExceptionsSpecification;
import Abstract.Strategies.OutputResultsConversionStrategies.ISearchResultsConvertStrategy;
import Services.DIResolver;
import Services.GuiService;
import Services.SettingsService;
import Utils.StrUtils;
import org.apache.commons.lang.StringUtils;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;

public class ConvertSearchResultsDataStrategy implements ISearchResultsConvertStrategy<RegularSearchResultItem, IOutputModel> {

    private final DIResolver diResolver;
    public ConvertSearchResultsDataStrategy(DIResolver diResolver) {
        this.diResolver = diResolver;
    }

    @Override
    public List<IOutputModel> convertResultData(List<RegularSearchResultItem> searchItems) {
        ArrayList<IOutputModel> outputItems = new ArrayList<>();
        if (searchItems.size() == 0) {
            return null;
        }
        GuiService guiService = diResolver.getGuiService();
        SettingsService settingsService = diResolver.getSettingsService();
        MetaTagsExceptionsSpecification metaTagsExceptionsSpecification = new MetaTagsExceptionsSpecification(settingsService.getSearchSettings().metaTagsExceptions);
        int searchedItemsSize = searchItems.size();

        for (int i = 0; i < searchedItemsSize; i++) {
            guiService.updateCountItemsStatus(i, searchedItemsSize);
            WebPageObject webPageObject = getWebSitePageSource(searchItems.get(i));
            if (webPageObject != null && metaTagsExceptionsSpecification.isSatisfiedBy(webPageObject)) {
                String mainHeader = getMainHeader(webPageObject, searchItems.get(i));
                String notSureLink = getNotSureLink(searchItems.get(i));
                String webSite = getWebSite(searchItems.get(i));
                String htmlPageTitle = getHtmlPageTitle(webPageObject, searchItems.get(i));
                OutputRegularCSVItem outputRegularCSVItem = new OutputRegularCSVItem(mainHeader, webSite, notSureLink, htmlPageTitle);
                outputItems.add(outputRegularCSVItem);
            }
        }
        return outputItems;
    }

    private String getMainHeader(WebPageObject webPageObject, GoogleSearchResultItem googleSearchResultItem) {
        if (StringUtils.isEmpty(googleSearchResultItem.getMainHeader())) {
            return webPageObject.getSiteName();
        }
        return googleSearchResultItem.getMainHeader();
    }

    private WebPageObject getWebSitePageSource(GoogleSearchResultItem item) {
        RequestData requestData = new RequestData(item.getLink());
        Element element = new WebUrlEngine().getWebSourceData(requestData);
        return parseSourceData(element);
    }

    private WebPageObject parseSourceData(Element html){
        if (html == null) {
            return null;
        }
        String siteDescription = html.select("meta[name=description]").attr("content");
        String siteKeywords = html.select("meta[name=keywords]").attr("content");
        String siteName = html.select("meta[property=og:title]").attr("content");
        if (StringUtils.isEmpty(siteName)) {
            siteName = html.select("title").text();
        }
        return new WebPageObject(siteDescription, siteKeywords, siteName);
    }

    private String getHtmlPageTitle(WebPageObject webPageObject, GoogleSearchResultItem googleSearchResultItem) {
        if (StringUtils.isEmpty(webPageObject.getSiteName())) {
            return googleSearchResultItem.getMainHeader();
        }
        return webPageObject.getSiteName();
    }

    private String getNotSureLink(GoogleSearchResultItem googleSearchResultItem) {
        String notSureLink = "";
        String urlPath = StrUtils.getUnmatchedPartOfString(googleSearchResultItem.getLink());
        if (urlPath.length() > 15){
            notSureLink = googleSearchResultItem.getLink();
        }
        return notSureLink;
    }

    private String getWebSite(GoogleSearchResultItem googleSearchResultItem) {
        String webSite = "";
        String urlPath = StrUtils.getUnmatchedPartOfString(googleSearchResultItem.getLink());
        if (urlPath.length() < 15){
            webSite = StrUtils.extractDomainName(googleSearchResultItem.getLink());
        }
        return webSite;
    }
}
