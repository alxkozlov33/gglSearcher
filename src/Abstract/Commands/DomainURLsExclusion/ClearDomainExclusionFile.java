package Abstract.Commands.DomainURLsExclusion;

import Abstract.Commands.AbstractCommandAction;
import Services.DBConnectionService;
import Services.DIResolver;
import Services.GuiService;
import Services.InputDataService;
import org.tinylog.Logger;

import java.awt.event.ActionEvent;

public class ClearDomainExclusionFile extends AbstractCommandAction {

    private final DIResolver diResolver;

    @Override
    public void actionPerformed(ActionEvent e) {
        Logger.tag("SYSTEM").info("Input exclusions URLS data file removed");
        GuiService guiService = diResolver.getGuiService();
        InputDataService inputDataService = diResolver.getInputDataService();
        DBConnectionService dbConnectionService = diResolver.getDbConnectionService();

        inputDataService.clearInputExclsuionURLsDataFile();
        guiService.clearInputExclsuionsDataFilePath();
        dbConnectionService.updateExclusionURLsFileDataPath("");
    }

    public ClearDomainExclusionFile(DIResolver diResolver) {
        super("Clear");
        this.diResolver = diResolver;
    }
}