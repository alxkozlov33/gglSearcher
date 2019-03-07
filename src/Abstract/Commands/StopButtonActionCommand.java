package Abstract.Commands;

import Services.DIResolver;
import Services.GuiService;
import Services.PropertiesService;
import org.pmw.tinylog.Logger;
import java.awt.event.ActionEvent;

public class StopButtonActionCommand extends AbstractCommandAction {

    private final DIResolver diResolver;
    public StopButtonActionCommand(DIResolver diResolver) {
        super("Stop");
        this.diResolver = diResolver;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        PropertiesService propertiesService = diResolver.getPropertiesService();
        GuiService guiService = diResolver.getGuiService();

        propertiesService.saveWorkState(false);
        propertiesService.saveIndex(0);
        Logger.info("Stopping...");
        guiService.setStatusText("Stopping...");

    }
}
