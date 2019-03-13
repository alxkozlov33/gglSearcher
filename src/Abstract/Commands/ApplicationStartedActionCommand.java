package Abstract.Commands;

import Services.*;
import Utils.DirUtils;
import org.tinylog.Logger;
import java.awt.event.ActionEvent;
import java.io.File;

public class ApplicationStartedActionCommand extends AbstractCommandAction {

    private final DIResolver diResolver;

    public ApplicationStartedActionCommand(DIResolver diResolver) {
        super( "");
        this.diResolver = diResolver;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Logger.tag("SYSTEM").info("Application started");

        diResolver.getUserAgentsRotatorService().initList();
        PropertiesService propertiesService = diResolver.getPropertiesService();
        GuiService guiService = diResolver.getGuiService();
        OutputDataService outputDataService = diResolver.getOutputDataService();
        InputDataService inputDataService = diResolver.getInputDataService();
        SettingsService settingsService = diResolver.getSettingsService();
        SearchService searchService = diResolver.getSearchService();

        String placeholder = propertiesService.getPlaceHolder();
        guiService.setPlaceholder(placeholder);

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

        if (propertiesService.getWorkState()
               && DirUtils.isFileOk(inputDataService.getInputDataFile(), "csv")
               && DirUtils.isDirOk(outputDataService.getOutputFolder())
               && DirUtils.isFileOk(settingsService.getSettingsDataFile(), "txt")) {
            searchService.StartWork(diResolver);
        }
    }
}
