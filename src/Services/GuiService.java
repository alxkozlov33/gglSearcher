package Services;


import GUI.*;

import javax.swing.text.BadLocationException;

public class GuiService {
    private Bootstrapper bootstrapper;

    private final PropertiesService propertiesService;

    public GuiService(Bootstrapper frame) {
        propertiesService = DIResolver.getPropertiesService();
        bootstrapper = frame;
    }

    public Bootstrapper getBootstrapper() {
        return bootstrapper;
    }

    public void logAction(String message) {
        try {
            bootstrapper.getAutoScrollTextArea().append(message + "\n");
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    public String getSearchPlaceholderText() {
        return bootstrapper.getSearchingPlaceHolder().getText();
    }

    public void setInputFilePath(String path) {
        bootstrapper.getSelectedFileLabelData().setText(cutPath(path));
    }

    public void setInputExceptionsFilePath(String path) {
        bootstrapper.getExceptionsLabelFileData().setText(cutPath(path));
    }

    public void setPlaceholder(String placeholder) {
        bootstrapper.getSearchingPlaceHolder().setText(placeholder);
    }

    public void changeApplicationStateToWork(boolean isWorkState) {
        bootstrapper.getRunButton().setEnabled(!isWorkState);
        bootstrapper.getStopButton().setEnabled(isWorkState);
        bootstrapper.getSearchingPlaceHolder().setEnabled(!isWorkState);
        bootstrapper.getExceptionsFile().setEnabled(!isWorkState);
        bootstrapper.getInputData().setEnabled(!isWorkState);
        propertiesService.saveWorkState(isWorkState);
        if (!isWorkState) {
            propertiesService.saveIndex(0);
        }
    }

    private String cutPath(String path) {
        int size = 120;
        if (path.length() <= size) {
            return path;
        } else if (path.length() > size) {
            return "..."+path.substring(path.length() - (size - 3));
        } else {
            // whatever is appropriate in this case
            throw new IllegalArgumentException("Something wrong with file path cut");
        }
    }
}
