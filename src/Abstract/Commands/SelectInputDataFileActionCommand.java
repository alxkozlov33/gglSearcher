package Abstract.Commands;

import Services.*;
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
        DBConnectionService dbConnectionService = diResolver.getDbConnectionService();
        InputDataService inputDataService = diResolver.getInputDataService();

        File inputDataAbsolutePath = DirUtils.selectFileDialog(guiService.getMainFrame(), "Select CSV data file", "csv");
        if (DirUtils.isFileOk(inputDataAbsolutePath, "csv")) {
            guiService.setInputFilePath(inputDataAbsolutePath);
            dbConnectionService.updateFileDataPath(inputDataAbsolutePath.getAbsolutePath());
            inputDataService.initInputFile(inputDataAbsolutePath);
        }
    }

    public SelectInputDataFileActionCommand(DIResolver diResolver) {
        super("Choose new input file");
        this.diResolver = diResolver;
    }
}
