package Abstract.Strategies.OutputResultsConversionStrategies.MultipleSearchResultsDataConvertStrategy;

import Abstract.Engines.WebUrlEngine;
import Abstract.Models.OutputModels.IOutputModel;
import Abstract.Models.OutputModels.OutputModelGeoDataDecorator;
import Abstract.Models.OutputModels.OutputRegularCSVItem;
import Abstract.Models.RequestData;
import Abstract.Models.SearchResultModels.GoogleSearchResultItem;
import Abstract.Models.SearchResultModels.RegularSearchResultItem;
import Abstract.Models.SearchResultModels.WebPageObject;
import Abstract.Specifications.Concrete.MetaTagsExceptionsSpecification;
import Abstract.Specifications.Concrete.SpecificWordInPageSpecification;
import Abstract.Strategies.OutputResultsConversionStrategies.ISearchResultsConvertStrategy;
import Services.DIResolver;
import Services.PropertiesService;
import Services.SettingsService;
import Utils.StrUtils;
import org.apache.commons.lang.StringUtils;
import org.jsoup.nodes.Element;
import java.util.ArrayList;
import java.util.List;


//TODO: remove getting additional page source data
public class ConvertSearchResultsWithGeoDataStrategy implements ISearchResultsConvertStrategy<RegularSearchResultItem, IOutputModel> {

    private String city;
    private String country;
    private final DIResolver diResolver;

    public ConvertSearchResultsWithGeoDataStrategy(DIResolver diResolver, String city, String country) {
        this.city = city;
        this.country = country;
        this.diResolver = diResolver;
    }

    @Override
    public List<IOutputModel> convertResultData(List<RegularSearchResultItem> searchItems) {
        ArrayList<IOutputModel> outputItems = new ArrayList<>();
        if (searchItems.size() == 0) {
            return null;
        }

        SettingsService settingsService = diResolver.getSettingsService();
        PropertiesService propertiesService = diResolver.getPropertiesService();
        MetaTagsExceptionsSpecification metaTagsExceptionsSpecification = new MetaTagsExceptionsSpecification(settingsService.getSearchSettings().metaTagsExceptions);
        SpecificWordInPageSpecification specificWordInPageSpecification = new SpecificWordInPageSpecification(settingsService.getSearchSettings().specificWordsToSearch);

        for (GoogleSearchResultItem googleSearchResultItem : searchItems) {
            if(propertiesService.getWorkState()) {
                WebPageObject webPageObject = getWebSitePageSource(googleSearchResultItem);
                if (webPageObject != null && metaTagsExceptionsSpecification.isSatisfiedBy(webPageObject) && specificWordInPageSpecification.isSatisfiedBy(webPageObject)) {
                    String galleryName = getGalleryName(webPageObject, googleSearchResultItem);
                    String notSureLink = getNotSureLink(googleSearchResultItem);
                    String webSite = getWebSite(googleSearchResultItem);
                    String htmlPageTitle = getHtmlPageTitle(webPageObject, googleSearchResultItem);
                    OutputRegularCSVItem outputRegularCSVItem = new OutputRegularCSVItem(galleryName, webSite, notSureLink, htmlPageTitle);
                    OutputModelGeoDataDecorator outputModelGeoDataDecorator = new OutputModelGeoDataDecorator(outputRegularCSVItem, city, country);
                    outputItems.add(outputModelGeoDataDecorator);
                }
            }
        }
        return outputItems;
    }

    private String getGalleryName(WebPageObject webPageObject, GoogleSearchResultItem googleSearchResultItem) {
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

    private WebPageObject parseSourceData(Element pageSourceData){
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
