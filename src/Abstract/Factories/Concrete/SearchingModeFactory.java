package Abstract.Factories.Concrete;
import Abstract.Strategies.Concrete.MultipleSearchModeStrategy;
import Abstract.Strategies.Concrete.SingleSearchModeStrategy;
import Abstract.Strategies.SearchModeStrategyBase;
import Services.DIResolver;
import Utils.DirUtils;
import Utils.StrUtils;

public class SearchingModeFactory {

    private final DIResolver diResolver;

    public SearchingModeFactory(DIResolver diResolver) {
        this.diResolver = diResolver;
    }

    public SearchModeStrategyBase createSearchModeStrategy() {
        SearchModeStrategyBase searchModeStrategy = null;

        String placeHolder = diResolver.getGuiService().getSearchPlaceholderText();
        if (diResolver.getPropertiesService().getWorkState()
                && DirUtils.isDirOk(diResolver.getOutputDataService().getOutputFolder())
                && DirUtils.isFileOk(diResolver.getSettingsService().getSettingsDataFile(), "txt")) {

            if (DirUtils.isFileOk(diResolver.getInputDataService().getInputDataFile(), "csv")
                    || StrUtils.isPlaceholderHasSubstituteTerms(placeHolder)) {
                searchModeStrategy = new MultipleSearchModeStrategy();
            }
            else {
                searchModeStrategy = new SingleSearchModeStrategy();
            }
        }
        return searchModeStrategy;
    }
}
