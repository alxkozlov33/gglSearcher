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
        SettingsService settingsService = diResolver.getSettingsService();

        guiService.setStatusText("Starting...");

        String placeholder = guiService.getSearchPlaceholderText();
        propertiesService.savePlaceHolder(placeholder);

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
                guiService.setStatusText("Finished");
                propertiesService.saveWorkState(false);
                propertiesService.saveIndex(0);
            } catch (Exception ex) {
                Logger.tag("SYSTEM").error(ex);
                Logger.tag("SYSTEM").info("Application aborted. Check your input files and placeholder.");
                guiService.setStatusText("Application aborted. Check your input files and placeholder.");
            }
            guiService.changeApplicationStateToWork(false);
        });
        worker.start();
    }
}
