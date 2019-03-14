package Abstract.Commands;

import Abstract.Factories.Concrete.SearchingModeFactory;
import Abstract.Strategies.SearchModeStrategyBase;
import Services.DIResolver;
import Services.GuiService;
import Services.PropertiesService;
import org.tinylog.Logger;

import java.awt.event.ActionEvent;

public class RunButtonActionCommand extends AbstractCommandAction {

    private final DIResolver diResolver;

    public RunButtonActionCommand(DIResolver diResolver) {
        super("Run");
        this.diResolver = diResolver;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Logger.tag("SYSTEM").info("Run button action performed");

        PropertiesService propertiesService = diResolver.getPropertiesService();
        GuiService guiService = diResolver.getGuiService();
        String placeHolderText = guiService.getSearchPlaceholderText();
        propertiesService.saveWorkState(true);

        Thread worker = new Thread(() -> {
            guiService.changeApplicationStateToWork(true);
            SearchingModeFactory searchingModeFactory = new SearchingModeFactory();
            SearchModeStrategyBase searchModeStrategy =  searchingModeFactory.createSearchModeStrategy(placeHolderText);
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
