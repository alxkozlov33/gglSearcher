package Abstract.Commands;

import Abstract.Exceptions.InputFileEmptyException;
import Abstract.Factories.SearchingModeFactory;
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
        DBConnectionService dbConnectionService = diResolver.getDbConnectionService();
        GuiService guiService = diResolver.getGuiService();
        guiService.setStatusText("Starting...");

        String placeholder = guiService.getSearchPlaceholderText();
        dbConnectionService.updateSearchPlaceholder(placeholder);

        dbConnectionService.updateWorkStatus(true);
        Thread worker = new Thread(() -> {
            guiService.changeApplicationStateToWork(true);
            SearchingModeFactory searchingModeFactory = new SearchingModeFactory(diResolver);
            SearchModeStrategyBase searchModeStrategy = searchingModeFactory.createSearchModeStrategy();
            try {
                diResolver.setCurrentWorker(searchModeStrategy);
                searchModeStrategy.processData();
                Logger.tag("SYSTEM").info("Finished");
                guiService.setStatusText("Finished");
                dbConnectionService.updateWorkStatus(false);
            } catch (Exception | InputFileEmptyException ex) {
                Logger.tag("SYSTEM").error(ex);
                Logger.tag("SYSTEM").info("Application aborted. Check your input files and placeholder.");
                guiService.setStatusText("Application aborted. Check your input files and placeholder.");
            }
            guiService.changeApplicationStateToWork(false);
        });
        worker.start();
    }
}
