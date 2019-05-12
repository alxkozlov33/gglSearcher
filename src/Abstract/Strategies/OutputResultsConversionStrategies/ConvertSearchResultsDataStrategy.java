package Abstract.Strategies.OutputResultsConversionStrategies;

import Abstract.Models.OutputModels.IOutputModel;
import Abstract.Models.OutputModels.OutputRegularCSVItem;
import Abstract.Models.SearchResultModels.GoogleSearchResultItem;
import Abstract.Models.SearchResultModels.RegularSearchResultItem;
import Abstract.Models.SearchResultModels.WebPageObject;
import Abstract.Specifications.Concrete.MetaTagsExceptionsSpecification;
import Abstract.Specifications.Concrete.SpecificWordInPageSpecification;
import Services.DIResolver;
import Services.GuiService;
import Services.PropertiesService;
import Services.SettingsService;
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
            return null;
        }
        GuiService guiService = diResolver.getGuiService();
        SettingsService settingsService = diResolver.getSettingsService();
        PropertiesService propertiesService = diResolver.getPropertiesService();
        MetaTagsExceptionsSpecification metaTagsExceptionsSpecification = new MetaTagsExceptionsSpecification(settingsService.getSearchSettings().MetaTagsExceptions);
        SpecificWordInPageSpecification specificWordInPageSpecification = new SpecificWordInPageSpecification(settingsService.getSearchSettings().KeywordsForLookingInSearchResults);
        int searchedItemsSize = searchItems.size();

        for (int i = 0; i < searchedItemsSize; i++) {
            if(propertiesService.getWorkState()) {
                guiService.updateCountItemsStatus(i, searchedItemsSize);
                Element pageSourceData = getWebSitePageSource(searchItems.get(i));
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

    private String getMainHeader(WebPageObject webPageObject, GoogleSearchResultItem googleSearchResultItem) {
        if (StringUtils.isEmpty(googleSearchResultItem.getMainHeader())) {
            return webPageObject.getSiteName();
        }
        return googleSearchResultItem.getMainHeader();
    }
}
