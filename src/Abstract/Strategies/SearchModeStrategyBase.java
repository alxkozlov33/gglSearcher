package Abstract.Strategies;

import Abstract.Models.SearchResultModels.GoogleSearchResultItem;
import Abstract.Specifications.AbstractSpecification;
import Abstract.Specifications.Concrete.DomainExceptionsSpecification;
import Abstract.Specifications.Concrete.TopLevelDomainExceptionsSpecification;
import Abstract.Specifications.Concrete.URLExceptionsSpecification;
import Services.DIResolver;
import Services.SettingsService;
import Utils.ResultsUtils;

import java.util.ArrayList;
import java.util.List;

public abstract class SearchModeStrategyBase {
    public abstract void processData(DIResolver diResolver);
    public abstract void stopProcessing();

    protected <T extends GoogleSearchResultItem> ArrayList filterGoogleResultData(List<T> googleSearchResults) {
        SettingsService settingsService = new SettingsService();

        AbstractSpecification<GoogleSearchResultItem> googleItemsSpec =
                new DomainExceptionsSpecification(settingsService.getSearchSettings().domainExceptions)
                        .and(new TopLevelDomainExceptionsSpecification(settingsService.getSearchSettings().topLevelDomainsExceptions))
                        .and(new URLExceptionsSpecification(settingsService.getSearchSettings().URLExceptions));

        return ResultsUtils.filterResults(googleSearchResults, googleItemsSpec);
    }
}
