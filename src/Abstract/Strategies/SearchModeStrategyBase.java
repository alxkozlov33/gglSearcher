package Abstract.Strategies;

import Abstract.Exceptions.InputFileEmptyException;
import Abstract.Models.SearchResultModels.GoogleSearchResultItem;
import Abstract.Models.SearchSettings;
import Abstract.Specifications.AbstractSpecification;
import Abstract.Specifications.Concrete.DomainExceptionsSpecification;
import Abstract.Specifications.Concrete.TopLevelDomainExceptionsSpecification;
import Abstract.Specifications.Concrete.URLExceptionsSpecification;
import Abstract.Specifications.Concrete.URLSpecificWordsSearchSpecification;
import Services.DIResolver;

public abstract class SearchModeStrategyBase {
    public abstract void processData() throws InputFileEmptyException;
    public abstract void stopProcessing();

    protected AbstractSpecification<GoogleSearchResultItem> getSettingsSpecification(DIResolver diResolver) {
        SearchSettings searchSettings = diResolver.getDbConnectionService().getSearchSettings();
        return new DomainExceptionsSpecification(searchSettings.ExceptionsForFoundDomains)
                .and(new TopLevelDomainExceptionsSpecification(searchSettings.ExceptionsForTopLevelDomains))
                .and(new URLExceptionsSpecification(searchSettings.ExceptionsForWordsInDomainURLs))
                .and(new URLSpecificWordsSearchSpecification(searchSettings.KeywordsForLookingInDomainURLs));
    }
}
