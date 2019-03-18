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
        PropertiesService propertiesService = diResolver.getPropertiesService();
        GuiService guiService = diResolver.getGuiService();

        String placeholder = propertiesService.getPlaceHolder();
        guiService.setPlaceholder(placeholder);

        File inputFile = propertiesService.getInputFile();
        if (DirUtils.isFileOk(inputFile, "csv")) {
            guiService.setInputFilePath(inputFile);
        }

        File settingsFile = propertiesService.getSettingsFilePath();
        if (DirUtils.isFileOk(settingsFile, "txt")) {
            guiService.setSettingsFilePath(settingsFile);
        }

        if (propertiesService.getWorkState()) {
            RunButtonActionCommand runButtonActionCommand = new RunButtonActionCommand(diResolver);
            runButtonActionCommand.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
        }
    }
}
