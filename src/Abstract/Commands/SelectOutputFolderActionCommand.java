package Abstract.Commands;

import Services.*;
import Utils.DirUtils;
import org.tinylog.Logger;

import java.awt.event.ActionEvent;
import java.io.File;

public class SelectOutputFolderActionCommand extends AbstractCommandAction {

    private final DIResolver diResolver;

    @Override
    public void actionPerformed(ActionEvent e) {
        Logger.tag("SYSTEM").info("Select input data file button action performed");
        GuiService guiService = diResolver.getGuiService();
        PropertiesService propertiesService = diResolver.getPropertiesService();
        OutputDataService outputDataService = diResolver.getOutputDataService();

        File outputFolder = DirUtils.selectFolderDialog(guiService.getMainFrame(), "Select output folder");
        if (DirUtils.isDirOk(outputFolder)) {
            outputDataService.setOutputFile(outputFolder);
            propertiesService.saveOutputFolderPath(outputFolder);
            guiService.setOutputFolder(outputDataService.getOutputFolder());
        }
    }

    public SelectOutputFolderActionCommand(DIResolver diResolver) {
        super("Choose output folder");
        this.diResolver = diResolver;
    }
}