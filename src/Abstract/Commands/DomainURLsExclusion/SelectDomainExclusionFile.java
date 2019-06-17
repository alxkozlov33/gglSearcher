package Abstract.Commands.DomainURLsExclusion;

import Abstract.Commands.AbstractCommandAction;
import Services.DBConnectionService;
import Services.DIResolver;
import Services.GuiService;
import Services.InputDataService;
import Utils.DirUtils;
import org.tinylog.Logger;

import java.awt.event.ActionEvent;
import java.io.File;

public class SelectDomainExclusionFile extends AbstractCommandAction {

    private final DIResolver diResolver;

    @Override
    public void actionPerformed(ActionEvent e) {
        Logger.tag("SYSTEM").info("Select input urls exclusion file button action performed");
        GuiService guiService = diResolver.getGuiService();
        DBConnectionService dbConnectionService = diResolver.getDbConnectionService();
        InputDataService inputDataService = diResolver.getInputDataService();

        File inputDataAbsolutePath = DirUtils.selectFileDialog(guiService.getMainFrame(), "Select URLs exclusion data file", "csv");
        if (DirUtils.isFileOk(inputDataAbsolutePath, "csv")) {
            guiService.setInputURLsExclusionDataFile(inputDataAbsolutePath);
            dbConnectionService.updateExclusionURLsFileDataPath(inputDataAbsolutePath.getAbsolutePath());
            inputDataService.initInputExclusionFile(inputDataAbsolutePath);
        }
    }

    public SelectDomainExclusionFile(DIResolver diResolver) {
        super("Upload CSV with domains to ignore");
        this.diResolver = diResolver;
    }
}
