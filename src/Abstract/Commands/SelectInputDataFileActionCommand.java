package Abstract.Commands;

import Services.DIResolver;
import java.awt.event.ActionEvent;

public class SelectInputDataFileActionCommand extends AbstractCommandAction {

    @Override
    public void actionPerformed(ActionEvent e) {
        fileService.SetInputFile(null);
        guiService.setInputFilePath(fileService.getInputFilePath());
        propertiesService.saveInputFilePath(fileService.getInputFilePath());
    }

    public SelectInputDataFileActionCommand(DIResolver diResolver) {
        super(diResolver,"Choose new input file");

    }
}
