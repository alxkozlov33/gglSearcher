package Services;


import GUI.*;

public class GuiService {
    private Bootstrapper bootstrapper;
    public GuiService(Bootstrapper frame) {
        bootstrapper = frame;
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
}
