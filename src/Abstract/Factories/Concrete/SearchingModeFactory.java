package Abstract.Factories.Concrete;
import Abstract.Strategies.Concrete.MultipleSearchModeStrategy;
import Abstract.Strategies.Concrete.SingleSearchModeStrategy;
import Abstract.Strategies.SearchModeStrategyBase;
import Utils.StrUtils;

public class SearchingModeFactory {

    public SearchingModeFactory() {
    }

    public SearchModeStrategyBase createSearchModeStrategy(String placeholderText) {
        SearchModeStrategyBase searchModeStrategy = null;

        if (StrUtils.isPlaceholderHasSubstituteTerms(placeholderText)) {
            searchModeStrategy = new MultipleSearchModeStrategy();
        }
        else if (!StrUtils.isPlaceholderHasSubstituteTerms(placeholderText)) {
            searchModeStrategy = new SingleSearchModeStrategy();
        }
        return searchModeStrategy;
    }
}
