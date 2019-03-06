package Abstract.Commands;

import Services.DIResolver;
import java.awt.event.ActionEvent;

public class ClearSettingsFileActionCommand extends AbstractCommandAction {


    @Override
    public void actionPerformed(ActionEvent e) {
        fileService.clearExceptionsFile();
        guiService.setInputExceptionsFilePath("");
    }

    public ClearSettingsFileActionCommand(DIResolver diResolver) {
        super(diResolver,"Clear settings file");
    }
}
