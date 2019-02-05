package Services;


import GUI.*;

//Probably will be deleted
public class GuiService {
    private Bootstrapper bootstrapper;
    private PropertiesService propertiesService;
    public GuiService(Bootstrapper frame, PropertiesService propertiesService) {
        bootstrapper = frame;
        this.propertiesService = propertiesService;
    }

    public Bootstrapper getBootstrapper() {
        return bootstrapper;
    }

    public void logAction(String message) {
        bootstrapper.getLogWindow().append(message + "\n");
    }

    public void ScrollLogWindow() {
    }

    public void setInputFilePath(String path) {
        bootstrapper.getSelectedFileLabelData().setText(cutPath(path));
    }

    public void changeApplicationStateToWork(boolean isWorkState) {
        bootstrapper.getRunButton().setEnabled(!isWorkState);
        bootstrapper.getStopButton().setEnabled(isWorkState);
        bootstrapper.getSelectFileButton().setEnabled(!isWorkState);
        bootstrapper.getSearchingPlaceHolder().setEnabled(!isWorkState);
        propertiesService.saveWorkState(isWorkState);
        if (!isWorkState) {
            propertiesService.saveIndex(0);
        }
    }

    public void RestorePlaceholder() {
        bootstrapper.getSearchingPlaceHolder().setText(propertiesService.getPlaceHolder());
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
