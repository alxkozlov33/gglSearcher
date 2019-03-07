package Abstract.Strategies.Concrete;

import Abstract.OutputModels.OutputRegularCSVItem;
import Abstract.SearchResultModels.GoogleSearchResultItem;
import Abstract.SearchResultModels.WebPageObject;
import Abstract.Specifications.Concrete.MetaTagsExceptionsSpecification;
import Abstract.Strategies.ISearchResultsConvertStrategy;
import Services.SearchService;
import Services.SettingsService;
import Utils.StrUtils;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class ConvertSearchResultsWithGeoDataStrategy implements ISearchResultsConvertStrategy<GoogleSearchResultItem, OutputRegularCSVItem> {

    private String city;
    private String country;

    public ConvertSearchResultsWithGeoDataStrategy(String city, String country) {
        this.city = city;
        this.country = country;
    }

    @Override
    public List<OutputRegularCSVItem> convertResultData(List<GoogleSearchResultItem> searchItems) {
        ArrayList<OutputRegularCSVItem> outputItems = new ArrayList<>();
        if (searchItems.size() == 0) {
            return null;
        }
        SearchService searchService = new SearchService();
        SettingsService settingsService = new SettingsService();
        MetaTagsExceptionsSpecification metaTagsExceptionsSpecification = new MetaTagsExceptionsSpecification(settingsService.getSearchSettings().metaTagsExceptions);

        for (GoogleSearchResultItem googleSearchResultItem : searchItems) {

            WebPageObject webPageObject = searchService.getWebSitePageSource(googleSearchResultItem);
            metaTagsExceptionsSpecification.isSatisfiedBy(webPageObject);

            if (metaTagsExceptionsSpecification.isSatisfiedBy(webPageObject)) {
                String galleryName = getGalleryName(webPageObject, googleSearchResultItem);
                String notSureLink = getNotSureLink(googleSearchResultItem);
                String webSite = getWebSite(googleSearchResultItem);
                OutputRegularCSVItem outputRegularCSVItem = new OutputRegularCSVItem(galleryName, webSite, city, notSureLink, country);
                outputItems.add(outputRegularCSVItem);
            }
        }
        return outputItems;
    }

    private String getGalleryName(WebPageObject webPageObject, GoogleSearchResultItem googleSearchResultItem) {
        if (StringUtils.isEmpty(webPageObject.getSiteName())) {
            return webPageObject.getSiteName();
        }
        return googleSearchResultItem.getMainHeader();
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
