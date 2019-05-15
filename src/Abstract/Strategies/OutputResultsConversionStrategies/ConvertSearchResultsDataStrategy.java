package Abstract.Strategies.OutputResultsConversionStrategies;

import Abstract.Models.OutputModels.IOutputModel;
import Abstract.Models.OutputModels.OutputModelGeoDataDecorator;
import Abstract.Models.OutputModels.OutputRegularCSVItem;
import Abstract.Models.SearchResultModels.GoogleSearchResultItem;
import Abstract.Models.SearchResultModels.RegularSearchResultItem;
import Abstract.Models.SearchResultModels.WebPageObject;
import Abstract.Specifications.Concrete.MetaTagsExceptionsSpecification;
import Abstract.Specifications.Concrete.SpecificWordInPageSpecification;
import Services.*;
import kbaa.gsearch.PlaceCard;
import org.apache.commons.lang.StringUtils;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;

public class ConvertSearchResultsDataStrategy extends SearchResultsConvertStrategy<RegularSearchResultItem, IOutputModel> {

    private final DIResolver diResolver;
    public ConvertSearchResultsDataStrategy(DIResolver diResolver) {
        this.diResolver = diResolver;
    }

    @Override
    public synchronized List<IOutputModel> convertResultDataToOutputModels(List<RegularSearchResultItem> searchItems) {
        ArrayList<IOutputModel> outputItems = new ArrayList<>();
        if (searchItems.size() == 0) {
            return outputItems;
        }
        GuiService guiService = diResolver.getGuiService();
        SettingsService settingsService = diResolver.getSettingsService();
        DBConnectionService dbConnectionService = diResolver.getDbConnectionService();
        MetaTagsExceptionsSpecification metaTagsExceptionsSpecification = new MetaTagsExceptionsSpecification(settingsService.getSearchSettings().MetaTagsExceptions);
        SpecificWordInPageSpecification specificWordInPageSpecification = new SpecificWordInPageSpecification(settingsService.getSearchSettings().KeywordsForLookingInSearchResults);
        int searchedItemsSize = searchItems.size();

        for (int i = 0; i < searchedItemsSize; i++) {
            if(dbConnectionService.getWorkStatus()) {
                guiService.updateCountItemsStatus(i, searchedItemsSize);
                Element pageSourceData = getWebSitePageSource(searchItems.get(i), diResolver);
                WebPageObject webPageObject = parseSourceData(pageSourceData);
                if (webPageObject != null && metaTagsExceptionsSpecification.isSatisfiedBy(webPageObject) && specificWordInPageSpecification.isSatisfiedBy(webPageObject)) {
                    String mainHeader = getMainHeader(webPageObject, searchItems.get(i));
                    String notSureLink = getNotSureLink(searchItems.get(i));
                    String webSite = getWebSite(searchItems.get(i));
                    String htmlPageTitle = getHtmlPageTitle(webPageObject, searchItems.get(i));
                    OutputRegularCSVItem outputRegularCSVItem = new OutputRegularCSVItem(mainHeader, webSite, notSureLink, htmlPageTitle);
                    outputItems.add(outputRegularCSVItem);
                }
            }
        }
        return outputItems;
    }

    @Override
    public List<IOutputModel> convertMapsResultDataToOutputModels(List<PlaceCard> searchItems) {
        ArrayList<IOutputModel> outputItems = new ArrayList<>();
        if (searchItems.size() == 0) {
            return outputItems;
        }

        DBConnectionService dbConnectionService = diResolver.getDbConnectionService();

        for (PlaceCard placeCard : searchItems) {
            if (dbConnectionService.getWorkStatus()) {
                OutputRegularCSVItem outputRegularCSVItem = new OutputRegularCSVItem(placeCard.getName(), placeCard.getSite(), placeCard.getSite(), placeCard.getDescription());
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
}
