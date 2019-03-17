package Abstract.Factories.SearchingModeStrategyFactory;
import Abstract.Strategies.SearchingModeStrategies.MultipleSearchModeStrategy;
import Abstract.Strategies.SearchingModeStrategies.SingleSearchModeStrategy;
import Abstract.Strategies.SearchModeStrategyBase;
import Services.DIResolver;
import Services.GuiService;
import Services.OutputDataService;
import Utils.DirUtils;
import Utils.StrUtils;

public class SearchingModeFactory {

    private final DIResolver diResolver;

    public SearchingModeFactory(DIResolver diResolver) {
        this.diResolver = diResolver;
    }

    public SearchModeStrategyBase createSearchModeStrategy() {
        SearchModeStrategyBase searchModeStrategy = null;

        GuiService guiService = diResolver.getGuiService();
        OutputDataService outputDataService = diResolver.getOutputDataService();
        String placeHolder = diResolver.getGuiService().getSearchPlaceholderText();
        if (diResolver.getPropertiesService().getWorkState()
                && DirUtils.isDirOk(diResolver.getOutputDataService().getOutputFolder())
                && DirUtils.isFileOk(diResolver.getSettingsService().getSettingsDataFile(), "txt")) {

            if (DirUtils.isFileOk(diResolver.getInputDataService().getInputDataFile(), "csv") || StrUtils.isPlaceholderHasSubstituteTerms(placeHolder)) {
                searchModeStrategy = new MultipleSearchModeStrategy();
                outputDataService.createOutputFileForMultipleSearchOutput(guiService.getSearchPlaceholderText());
            }
            else {
                searchModeStrategy = new SingleSearchModeStrategy();
                outputDataService.createOutputFileForSingleSearchOutput(guiService.getSearchPlaceholderText());
            }
        }
        return searchModeStrategy;
    }
}
