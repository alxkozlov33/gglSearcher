package Abstract.Strategies;

import Abstract.Exceptions.InputFileEmptyException;
import Abstract.Models.SearchResultModels.GoogleSearchResultItem;
import Abstract.Specifications.AbstractSpecification;
import Abstract.Specifications.Concrete.DomainExceptionsSpecification;
import Abstract.Specifications.Concrete.TopLevelDomainExceptionsSpecification;
import Abstract.Specifications.Concrete.URLExceptionsSpecification;
import Abstract.Specifications.Concrete.URLSpecificWordsSearchSpecification;
import Services.DIResolver;
import Services.SettingsService;
import Utils.ResultsUtils;

import java.util.ArrayList;
import java.util.List;

public abstract class SearchModeStrategyBase {
    public abstract void processData() throws InputFileEmptyException;
    public abstract void stopProcessing();

    protected AbstractSpecification<GoogleSearchResultItem> getSettingsSpecification() {
        SettingsService settingsService = new SettingsService();
        return new DomainExceptionsSpecification(settingsService.getSearchSettings().domainExceptions)
                .and(new TopLevelDomainExceptionsSpecification(settingsService.getSearchSettings().topLevelDomainsExceptions))
                .and(new URLExceptionsSpecification(settingsService.getSearchSettings().URLExceptions))
                .and(new URLSpecificWordsSearchSpecification(settingsService.getSearchSettings().URLSpecificWordsSearching));
    }
}
