package Services;


import GUI.*;

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

    public void setInputFilePath(String path) {
        bootstrapper.getSelectedFileLabelData().setText(path);
    }

    public void changeApplicationStateToWork(boolean isWorkState) {
        bootstrapper.getRunButton().setEnabled(!isWorkState);
        bootstrapper.getStopButton().setEnabled(isWorkState);
        bootstrapper.getSelectFileButton().setEnabled(!isWorkState);
        bootstrapper.getSearchingPlaceHolder().setEnabled(isWorkState);
        propertiesService.saveWorkState(isWorkState);
        propertiesService.saveIndex(0);

//        isWorkFlag = true;
//        if (!isWorkState) {
//            propertiesObject.saveProperty("index", "0");
//            if (!isError) {
//                Main.gui.getLabelStatusData().setText("Finished");
//            }
//        }
    }
}
