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

        String placeHolderTerm = propertiesService.getPlaceHolder();

        File outputFolder = DirUtils.selectFolderDialog(guiService.getMainFrame(), "Select output folder");
        outputDataService.setOutputFile(outputFolder, placeHolderTerm);
        propertiesService.saveOutputFolderPath(outputFolder);
        guiService.setOutputFolder(outputDataService.getOutputFile());
    }

    public SelectOutputFolderActionCommand(DIResolver diResolver) {
        super("Choose output folder");
        this.diResolver = diResolver;
    }
}