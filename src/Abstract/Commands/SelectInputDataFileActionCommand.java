package Abstract.Commands;

import Services.DIResolver;
import Services.GuiService;
import Services.InputDataService;
import Services.PropertiesService;
import Utils.DirUtils;
import org.tinylog.Logger;
import java.awt.event.ActionEvent;
import java.io.File;

public class SelectInputDataFileActionCommand extends AbstractCommandAction {

    private final DIResolver diResolver;

    @Override
    public void actionPerformed(ActionEvent e) {
        Logger.tag("SYSTEM").info("Select input data file button action performed");
        GuiService guiService = diResolver.getGuiService();
        PropertiesService propertiesService = diResolver.getPropertiesService();
        InputDataService inputDataService = diResolver.getInputDataService();

        File inputDataAbsolutePath = DirUtils.selectFileDialog(guiService.getMainFrame(), "Select CSV data file", "csv");
        if (DirUtils.isFileOk(inputDataAbsolutePath, "csv")) {
            guiService.setInputFilePath(inputDataAbsolutePath);
            propertiesService.saveInputFilePath(inputDataAbsolutePath);
            inputDataService.initInputFile(inputDataAbsolutePath);
        }
    }

    public SelectInputDataFileActionCommand(DIResolver diResolver) {
        super("Choose new data file");
        this.diResolver = diResolver;
    }
}
