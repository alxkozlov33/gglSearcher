package Abstract.Commands;

import Services.DIResolver;
import java.awt.event.ActionEvent;

public class StopButtonActionCommand extends AbstractCommandAction {

    public StopButtonActionCommand(DIResolver diResolver) {
        super(diResolver,"Stop");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        propertiesService.saveWorkState(false);
        propertiesService.saveIndex(0);
        logService.LogMessage("Stopping...");
        guiService.setStatusText("Stopping...");
        searchService.setWorkStateToStop();
    }
}
