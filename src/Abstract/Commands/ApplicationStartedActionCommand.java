package Abstract.Commands;

import Services.*;
import Utils.DirUtils;
import org.tinylog.Logger;
import java.awt.event.ActionEvent;
import java.io.File;

public class ApplicationStartedActionCommand extends AbstractCommandAction {

    private final DIResolver diResolver;

    public ApplicationStartedActionCommand(DIResolver diResolver) {
        super("");
        this.diResolver = diResolver;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Logger.tag("SYSTEM").info("Application started");

        diResolver.getUserAgentsRotatorService().initList();
        DBConnectionService dbConnectionService = diResolver.getDbConnectionService();
        GuiService guiService = diResolver.getGuiService();

        String placeholder = dbConnectionService.getSearchPlaceholder();
        guiService.setPlaceholder(placeholder);

        File inputFile = new File(dbConnectionService.getDataFilePath());
        if (DirUtils.isFileOk(inputFile, "csv")) {
            guiService.setInputFilePath(inputFile);
        }
    }
}
