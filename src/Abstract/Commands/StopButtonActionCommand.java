package Abstract.Commands;

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
        diResolver.getCurrentWorker().stopProcessing();
        diResolver.getGuiService().setStatusText("Stopping...");
        diResolver.getPropertiesService().saveWorkState(false);
    }
}
