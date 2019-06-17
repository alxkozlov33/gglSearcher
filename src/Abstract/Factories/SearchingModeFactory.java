package Abstract.Factories;
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
        DBConnectionService dbConnectionService = diResolver.getDbConnectionService();
        String placeHolder = diResolver.getGuiService().getSearchPlaceholderText();

        File inputDataFile = new File(dbConnectionService.getDataFilePath());
        if (DirUtils.isFileOk(inputDataFile, "csv")) {
            inputDataService.initInputFile(inputDataFile);
        }

        File inputFile = new File(dbConnectionService.getDataFilePath());
        File inputUrlsExclusion = new File(dbConnectionService.getExclusionURLsFileDataPath());

        if (diResolver.getDbConnectionService().getWorkStatus()) {

            if (DirUtils.isFileOk(inputFile, "csv") && StrUtils.isPlaceholderHasSubstituteTerms(placeHolder)) {
                searchModeStrategy = new MultipleSearchModeStrategy(diResolver);
                inputDataService.initInputFile(inputFile);
                inputDataService.initInputFileData();
                inputDataService.initInputExclusionFile(inputUrlsExclusion);
                inputDataService.initInputExclusionFileData();
                outputDataService.createOutputFileForMultipleSearchOutput(guiService.getSearchPlaceholderText());
            }
            else if (!StrUtils.isPlaceholderHasSubstituteTerms(placeHolder)) {
                searchModeStrategy = new SingleSearchModeStrategy(diResolver);
                outputDataService.createOutputFileForSingleSearchOutput(guiService.getSearchPlaceholderText());
            }
        }
        return searchModeStrategy;
    }
}
