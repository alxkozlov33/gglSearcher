package Abstract.Commands;

import Models.SearchSettings;
import Services.DIResolver;
import Utils.StrUtils;

import java.awt.event.ActionEvent;
import java.util.ArrayList;

public class ApplicationStartedActionCommand extends AbstractCommandAction {

    public ApplicationStartedActionCommand(DIResolver diResolver) {
        super(diResolver, "");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        userAgentsRotatorService.initList();
        SearchSettings searchExceptions = null;
        ArrayList inputCsvData = null;

        String placeholderTerm = propertiesService.getPlaceHolder();
        String inputFile = propertiesService.getInputFilePath();
        if (fileService.SetInputFile(inputFile)) {
            inputCsvData = fileService.InitCSVItems();
            guiService.setInputFilePath(fileService.getInputFilePath());
        } else {
            placeholderTerm = StrUtils.clearPlaceholderFromCSVColumnsTerms(placeholderTerm);
        }

        guiService.setPlaceholder(placeholderTerm);
        fileService.SetOutputFile(placeholderTerm);

        String exceptionsFile = propertiesService.getExceptionsFilePath();
        if (fileService.SetExceptionsFile(exceptionsFile)) {
            //searchExceptions = fileService.initExceptionsKeywords();
            guiService.setInputExceptionsFilePath(exceptionsFile);
        }

        if (propertiesService.getWorkState()) {
            searchService.DoWork(inputCsvData, searchExceptions);
        }
    }
}
