package Abstract.Commands;

import Services.DIResolver;
import java.awt.event.ActionEvent;

public class SelectSettingsFileActionCommand extends AbstractCommandAction {

    public SelectSettingsFileActionCommand(DIResolver diResolver) {
        super(diResolver, "Choose new exceptions file");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        fileService.SetExceptionsFile(null);
        guiService.setInputExceptionsFilePath(fileService.getExceptionsFilePath());
        propertiesService.saveInputFilePath(fileService.getInputFilePath());
    }
}
