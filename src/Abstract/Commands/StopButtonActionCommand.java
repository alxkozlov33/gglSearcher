package Abstract.Commands;

import Abstract.Strategies.SearchModeStrategyBase;
import Services.DIResolver;
import org.tinylog.Logger;

import java.awt.event.ActionEvent;

public class StopButtonActionCommand extends AbstractCommandAction {

    private final DIResolver diResolver;
    public StopButtonActionCommand(DIResolver diResolver) {
        super("Stop");
        this.diResolver = diResolver;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Logger.tag("SYSTEM").info("Stop button action performed");
        SearchModeStrategyBase searchMode = diResolver.getCurrentWorker();
        if (searchMode != null) {
            searchMode.stopProcessing();
            diResolver.getGuiService().setStatusText("Stopping...");
            diResolver.getDbConnectionService().updateWorkStatus(false);
        }
    }
}
