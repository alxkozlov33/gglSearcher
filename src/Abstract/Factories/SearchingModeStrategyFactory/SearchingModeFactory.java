package Abstract.Factories.SearchingModeStrategyFactory;
import Abstract.Strategies.SearchingModeStrategies.MultipleSearchModeStrategy;
import Abstract.Strategies.SearchingModeStrategies.SingleSearchModeStrategy;
import Abstract.Strategies.SearchModeStrategyBase;
import Services.*;
import Utils.DirUtils;
import Utils.StrUtils;

import java.io.File;

public class SearchingModeFactory {

    private final DIResolver diResolver;

    public SearchingModeFactory(DIResolver diResolver) {
        this.diResolver = diResolver;
    }

    public SearchModeStrategyBase createSearchModeStrategy() {
        SearchModeStrategyBase searchModeStrategy = null;

        GuiService guiService = diResolver.getGuiService();
        OutputDataService outputDataService = diResolver.getOutputDataService();
        InputDataService inputDataService = diResolver.getInputDataService();
        PropertiesService propertiesService = diResolver.getPropertiesService();
        String placeHolder = diResolver.getGuiService().getSearchPlaceholderText();

        if (diResolver.getPropertiesService().getWorkState()
                && DirUtils.isFileOk(diResolver.getSettingsService().getSettingsDataFile(), "txt")) {

            if (DirUtils.isFileOk(diResolver.getInputDataService().getInputDataFile(), "csv") && StrUtils.isPlaceholderHasSubstituteTerms(placeHolder)) {
                searchModeStrategy = new MultipleSearchModeStrategy();
                File inputFile = propertiesService.getInputFile();
                inputDataService.initInputFile(inputFile);
                inputDataService.initInputFileData();
                guiService.setInputFilePath(inputFile);
                outputDataService.createOutputFileForMultipleSearchOutput(guiService.getSearchPlaceholderText());
            }
            else if (!DirUtils.isFileOk(diResolver.getInputDataService().getInputDataFile(), "csv") && !StrUtils.isPlaceholderHasSubstituteTerms(placeHolder)) {
                searchModeStrategy = new SingleSearchModeStrategy();
                outputDataService.createOutputFileForSingleSearchOutput(guiService.getSearchPlaceholderText());
            }
        }
        return searchModeStrategy;
    }
}
