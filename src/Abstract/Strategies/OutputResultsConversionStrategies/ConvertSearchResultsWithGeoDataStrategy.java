package Abstract.Strategies.OutputResultsConversionStrategies;

import Abstract.Models.OutputModels.IOutputModel;
import Abstract.Models.OutputModels.OutputModelGeoDataDecorator;
import Abstract.Models.OutputModels.OutputRegularCSVItem;
import Abstract.Models.SearchResultModels.GoogleSearchResultItem;
import Abstract.Models.SearchResultModels.RegularSearchResultItem;
import Abstract.Models.SearchResultModels.WebPageObject;
import Abstract.Specifications.Concrete.MetaTagsExceptionsSpecification;
import Abstract.Specifications.Concrete.SpecificWordInPageSpecification;
import Abstract.Strategies.OutputResultsConversionStrategies.SearchResultsConvertStrategy;
import Services.DIResolver;
import Services.PropertiesService;
import Services.SettingsService;
import Utils.StrUtils;
import org.apache.commons.lang.StringUtils;
import org.jsoup.nodes.Element;
import java.util.ArrayList;
import java.util.List;


//TODO: remove getting additional page source data
public class ConvertSearchResultsWithGeoDataStrategy extends SearchResultsConvertStrategy<RegularSearchResultItem, IOutputModel> {

    private String city;
    private String country;
    private final DIResolver diResolver;

    public ConvertSearchResultsWithGeoDataStrategy(DIResolver diResolver, String city, String country) {
        this.city = city;
        this.country = country;
        this.diResolver = diResolver;
    }

    @Override
    public List<IOutputModel> convertResultDataToOutputModels(List<RegularSearchResultItem> searchItems) {
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
                Element pageSourceData = getWebSitePageSource(googleSearchResultItem);
                WebPageObject webPageObject = parseSourceData(pageSourceData);
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
}
