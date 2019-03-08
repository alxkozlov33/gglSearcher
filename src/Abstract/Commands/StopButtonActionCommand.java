package Abstract.Commands;

import Services.DIResolver;
import Services.GuiService;
import Services.PropertiesService;
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
        PropertiesService propertiesService = diResolver.getPropertiesService();
        GuiService guiService = diResolver.getGuiService();

        propertiesService.saveWorkState(false);
        propertiesService.saveIndex(0);
        Logger.tag("SYSTEM").info("Stopping...");
        guiService.setStatusText("Stopping...");

    }
}
