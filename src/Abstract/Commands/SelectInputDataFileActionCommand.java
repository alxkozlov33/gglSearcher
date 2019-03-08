package Abstract.Commands;

import Services.DIResolver;
import Services.GuiService;
import Services.InputDataService;
import Services.PropertiesService;
import Utils.DirUtils;
import org.tinylog.Logger;
import java.awt.event.ActionEvent;

public class SelectInputDataFileActionCommand extends AbstractCommandAction {

    private final DIResolver diResolver;

    @Override
    public void actionPerformed(ActionEvent e) {
        Logger.tag("SYSTEM").info("Select input data file button action performed");
        GuiService guiService = diResolver.getGuiService();
        PropertiesService propertiesService = diResolver.getPropertiesService();
        InputDataService inputDataService = diResolver.getInputDataService();

        String inputDataAbsolutePath = DirUtils.selectFolderDialog(guiService.getMainFrame());
        guiService.setInputFilePath(inputDataService.getInputDataFile());
        propertiesService.saveInputFilePath(inputDataAbsolutePath);
        inputDataService.initCSVItems(inputDataAbsolutePath, guiService.getMainFrame());
    }

    public SelectInputDataFileActionCommand(DIResolver diResolver) {
        super("Choose new input file");
        this.diResolver = diResolver;
    }
}
