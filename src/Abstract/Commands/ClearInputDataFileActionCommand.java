package Abstract.Commands;

import Services.DIResolver;
import Utils.StrUtils;

import java.awt.event.ActionEvent;

public class ClearInputDataFileActionCommand extends AbstractCommandAction {

    @Override
    public void actionPerformed(ActionEvent e) {
        fileService.clearInputFile();
        guiService.setInputFilePath("");
        guiService.setPlaceholder(StrUtils.clearPlaceholderFromCSVColumnsTerms(guiService.getSearchPlaceholderText()));
    }

    public ClearInputDataFileActionCommand(DIResolver diResolver) {
        super(diResolver, "Clear current file");
    }
}
