package Abstract.Strategies.Concrete;

import Abstract.Models.OutputModels.IOutputModel;
import Abstract.Models.OutputModels.OutputModelGeoDataDecorator;
import Abstract.Models.OutputModels.OutputRegularCSVItem;
import Abstract.Models.SearchResultModels.GoogleSearchResultItem;
import Abstract.Models.SearchResultModels.WebPageObject;
import Abstract.Specifications.Concrete.MetaTagsExceptionsSpecification;
import Abstract.Strategies.ISearchResultsConvertStrategy;
import Services.SearchService;
import Services.SettingsService;
import Utils.StrUtils;
import org.apache.commons.lang.StringUtils;
import java.util.ArrayList;
import java.util.List;

public class ConvertSearchResultsWithGeoDataStrategy implements ISearchResultsConvertStrategy<GoogleSearchResultItem, IOutputModel> {

    private String city;
    private String country;

    ConvertSearchResultsWithGeoDataStrategy(String city, String country) {
        this.city = city;
        this.country = country;
    }

    @Override
    public List<IOutputModel> convertResultData(List<GoogleSearchResultItem> searchItems) {
        ArrayList<IOutputModel> outputItems = new ArrayList<>();
        if (searchItems.size() == 0) {
            return null;
        }
        SearchService searchService = new SearchService();
        SettingsService settingsService = new SettingsService();
        MetaTagsExceptionsSpecification metaTagsExceptionsSpecification = new MetaTagsExceptionsSpecification(settingsService.getSearchSettings().metaTagsExceptions);

        for (GoogleSearchResultItem googleSearchResultItem : searchItems) {
            WebPageObject webPageObject = searchService.getWebSitePageSource(googleSearchResultItem);
            if (webPageObject != null && metaTagsExceptionsSpecification.isSatisfiedBy(webPageObject)) {
                String galleryName = getGalleryName(webPageObject, googleSearchResultItem);
                String notSureLink = getNotSureLink(googleSearchResultItem);
                String webSite = getWebSite(googleSearchResultItem);
                String htmlPageTitle = getHtmlPageTitle(webPageObject, googleSearchResultItem);
                OutputRegularCSVItem outputRegularCSVItem = new OutputRegularCSVItem(galleryName, webSite, notSureLink, htmlPageTitle);
                OutputModelGeoDataDecorator outputModelGeoDataDecorator = new OutputModelGeoDataDecorator(outputRegularCSVItem, city, country);
                outputItems.add(outputModelGeoDataDecorator);
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
