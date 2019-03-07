package Abstract.Commands;

import Services.DIResolver;
import Services.GuiService;
import Services.InputDataService;
import Utils.StrUtils;
import java.awt.event.ActionEvent;

public class ClearInputDataFileActionCommand extends AbstractCommandAction {

    private final DIResolver diResolver;
    @Override
    public void actionPerformed(ActionEvent e) {
        GuiService guiService = diResolver.getGuiService();
        InputDataService inputDataService = diResolver.getInputDataService();

        inputDataService.clearInputDataFile();
        guiService.setInputFilePath("");
        guiService.setPlaceholder(StrUtils.clearPlaceholderFromCSVColumnsTerms(guiService.getSearchPlaceholderText()));
    }

    public ClearInputDataFileActionCommand(DIResolver diResolver) {
        super("Clear current file");
        this.diResolver = diResolver;
    }
}
