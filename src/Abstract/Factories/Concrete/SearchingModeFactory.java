package Abstract.Factories.Concrete;

import Abstract.Strategies.ISearchModeStrategy;
import Abstract.Strategies.Concrete.MultipleSearchModeStrategy;
import Abstract.Strategies.Concrete.SingleSearchModeStrategy;
import Utils.StrUtils;

public class SearchingModeFactory {
    public ISearchModeStrategy createSearchModeStrategy(String placeholderText) {
        ISearchModeStrategy searchModeStrategy = null;

        if (StrUtils.isPlaceholderHasSubstituteTerms(placeholderText)) {
            searchModeStrategy = new MultipleSearchModeStrategy();
        }
        else if (!StrUtils.isPlaceholderHasSubstituteTerms(placeholderText)) {
            searchModeStrategy = new SingleSearchModeStrategy();
        }
        return searchModeStrategy;
    }
}
