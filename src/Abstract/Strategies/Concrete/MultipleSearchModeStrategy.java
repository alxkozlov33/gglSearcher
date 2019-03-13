package Abstract.Strategies.Concrete;

import Abstract.Engines.WebUrlEngine;
import Abstract.Factories.Concrete.BusinessListResultsFactory;
import Abstract.Factories.Concrete.RegularResultsFactory;
import Abstract.Models.OutputModels.IOutputModel;
import Abstract.Models.SearchResultModels.GoogleSearchResultItem;
import Abstract.Models.SearchResultModels.BusinessListSearchResultItem;
import Abstract.Models.SearchResultModels.RegularSearchResultItem;
import Abstract.Specifications.AbstractSpecification;
import Abstract.Specifications.Concrete.DomainExceptionsSpecification;
import Abstract.Specifications.Concrete.TopLevelDomainExceptionsSpecification;
import Abstract.Specifications.Concrete.URLExceptionsSpecification;
import Abstract.Strategies.ISearchModeStrategy;
import Abstract.Strategies.ISearchResultsConvertStrategy;
import Abstract.Models.InputModels.InputCsvModelItem;
import Abstract.Models.RequestData;
import Services.*;
import Utils.ResultsUtils;
import Utils.StrUtils;
import org.jsoup.nodes.Element;
import org.tinylog.Logger;
import java.util.ArrayList;
import java.util.List;

public class MultipleSearchModeStrategy implements ISearchModeStrategy {
    private boolean isWorkFlag;

    public MultipleSearchModeStrategy() {
    }

    @Override
    public void processData(DIResolver diResolver) {
        GuiService guiService = new GuiService();
        InputDataService inputDataService = diResolver.getInputDataService();
        PropertiesService propertiesService = diResolver.getPropertiesService();
        OutputDataService outputDataService = diResolver.getOutputDataService();
        WebUrlEngine webUrlEngine = new WebUrlEngine();

        List<InputCsvModelItem> inputCsvItems = inputDataService.getInputCsvModelItems();

        int index = propertiesService.getIndex();

        isWorkFlag = true;
        Logger.tag("SYSTEM").info("Continue from: " + index + " record");
        for (int i = index; i < inputCsvItems.size(); i++) {
            InputCsvModelItem inputCsvModelItem = inputCsvItems.get(i);

            guiService.updateCountItemsStatus(i, inputCsvItems.size());
            if (!isWorkFlag) {
                break;
            }
            propertiesService.saveIndex(i);

            String URL = StrUtils.createURL(inputCsvModelItem, guiService.getSearchPlaceholderText());
            RequestData requestData = new RequestData(URL);
            Element body = webUrlEngine.getWebSourceData(requestData);
            if (body == null) {
                continue;
            }

            RegularResultsFactory regularResultsFactory = new RegularResultsFactory();
            List<RegularSearchResultItem> regularSearchResultItems = regularResultsFactory.processBody(body);
            List filteredRegularSearchResultItems = filterGoogleResultData(regularSearchResultItems);
            ISearchResultsConvertStrategy<RegularSearchResultItem, IOutputModel> regularConvertStrategy
                    = new ConvertSearchResultsWithGeoDataStrategy(diResolver, inputCsvModelItem.getColumnA(), inputCsvModelItem.getColumnC());

            BusinessListResultsFactory businessListResultsFactory = new BusinessListResultsFactory();
            List<BusinessListSearchResultItem> businessListSearchResultItems = businessListResultsFactory.processBody(body);
            List filteredListSearchResultItems = filterGoogleResultData(businessListSearchResultItems);
            ISearchResultsConvertStrategy<BusinessListSearchResultItem, IOutputModel> businessListConvertStrategy
                    = new ConvertBusinessSearchWithGeoDataStrategy(inputCsvModelItem.getColumnA(), inputCsvModelItem.getColumnC());

            //List regularItems = regularConvertStrategy.convertResultData(filteredRegularSearchResultItems);
            List listItems = businessListConvertStrategy.convertResultData(filteredListSearchResultItems);

            //outputDataService.saveResultCsvItems(regularItems);
            outputDataService.saveResultCsvItems(listItems);
        }
    }

    private <T extends GoogleSearchResultItem> ArrayList filterGoogleResultData(List<T> googleSearchResults) {
        SettingsService settingsService = new SettingsService();

        AbstractSpecification<GoogleSearchResultItem> googleItemsSpec =
                new DomainExceptionsSpecification(settingsService.getSearchSettings().domainExceptions)
                        .and(new TopLevelDomainExceptionsSpecification(settingsService.getSearchSettings().topLevelDomainsExceptions))
                        .and(new URLExceptionsSpecification(settingsService.getSearchSettings().URLExceptions));

        return ResultsUtils.filterResults(googleSearchResults, googleItemsSpec);
    }

    @Override
    public void stopProcessing() {
        isWorkFlag = false;
    }
}
