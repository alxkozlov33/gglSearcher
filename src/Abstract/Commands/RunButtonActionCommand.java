package Abstract.Commands;

import Abstract.Factories.SearchingModeStrategyFactory.SearchingModeFactory;
import Abstract.Strategies.SearchModeStrategyBase;
import Services.*;
import Utils.DirUtils;
import org.tinylog.Logger;

import java.awt.event.ActionEvent;
import java.io.File;

public class RunButtonActionCommand extends AbstractCommandAction {

    private final DIResolver diResolver;

    public RunButtonActionCommand(DIResolver diResolver) {
        super("Run");
        this.diResolver = diResolver;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Logger.tag("SYSTEM").info("Run button action performed");

        diResolver.getUserAgentsRotatorService().initList();
        PropertiesService propertiesService = diResolver.getPropertiesService();
        GuiService guiService = diResolver.getGuiService();
        OutputDataService outputDataService = diResolver.getOutputDataService();
        InputDataService inputDataService = diResolver.getInputDataService();
        SettingsService settingsService = diResolver.getSettingsService();

        String placeholder = guiService.getSearchPlaceholderText();

        File inputFile = propertiesService.getInputFile();
        if (DirUtils.isFileOk(inputFile, "csv")) {
            inputDataService.initInputFile(inputFile);
            inputDataService.initInputFileData();
            guiService.setInputFilePath(inputFile);
        }

        File outputFolderPath = propertiesService.getOutputFolderPath();
        if (DirUtils.isDirOk(outputFolderPath)) {
            guiService.setOutputFolder(outputFolderPath);
            outputDataService.setOutputFolder(outputFolderPath);
            outputDataService.createOutputFile(placeholder);
        }

        File settingsFile = propertiesService.getSettingsFilePath();
        if (DirUtils.isFileOk(settingsFile, "txt")) {
            guiService.setSettingsFilePath(settingsFile);
            settingsService.initSettingsFile(settingsFile);
            settingsService.initSettingsFileData();
        }

        propertiesService.saveWorkState(true);
        Thread worker = new Thread(() -> {
            guiService.changeApplicationStateToWork(true);
            SearchingModeFactory searchingModeFactory = new SearchingModeFactory(diResolver);
            SearchModeStrategyBase searchModeStrategy = searchingModeFactory.createSearchModeStrategy();
            try {
                diResolver.setCurrentWorker(searchModeStrategy);
                searchModeStrategy.processData(diResolver);
                Logger.tag("SYSTEM").info("Finished");
                guiService.setStatusText("Finished...");
                propertiesService.saveWorkState(false);
                propertiesService.saveIndex(0);
            } catch (Exception ex) {
                Logger.tag("SYSTEM").error(ex, "Application stopped");
            }
            guiService.changeApplicationStateToWork(false);
        });
        worker.start();
    }
}
