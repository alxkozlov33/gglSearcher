package Services;

import GUI.*;
import Utils.DirUtils;
import org.tinylog.Logger;
import java.awt.*;
import java.io.File;

public class GuiService {
    private Bootstrapper bootstrapper;
    private SettingsDialog settingsDialog;
    private AppSettings appSettings;

    public GuiService() {
    }

    public Frame getMainFrame() {
        return bootstrapper;
    }

    public void createNewSettingsDialog(DIResolver diResolver) {
        closeSettingsDialog();
        settingsDialog = new SettingsDialog(diResolver);
        settingsDialog.setSize(500, 700);
        settingsDialog.setTitle("Search settings");
        settingsDialog.setVisible(true);
    }

    public void createNewAppSettingsDialog(DIResolver diResolver) {
        closeAppSettings();
        appSettings = new AppSettings(diResolver);
        appSettings.setSize(500, 250);
        appSettings.setTitle("Preferences");
        appSettings.setVisible(true);
    }

    private void closeAppSettings() {
        if (appSettings != null) {
            appSettings.Dispose();
        }
    }

    private void closeSettingsDialog() {
        if (settingsDialog != null) {
            settingsDialog.onCancel();
        }
    }

    public void setBootstrapper(Bootstrapper bootstrapper) {
        this.bootstrapper = bootstrapper;
    }

    public String getSearchPlaceholderText(){
        return bootstrapper.getSearchingPlaceHolder().getText();
    }


    public void clearInputDataFilePath() {
        bootstrapper.getSelectedFileLabelData().setText("");
    }

    public void setInputFilePath(File file) {
        if (DirUtils.isFileOk(file, "csv")) {
            bootstrapper.getSelectedFileLabelData().setText(cutPath(file.getAbsolutePath()));
        }
    }

    public void setPlaceholder(String placeholder) {
        bootstrapper.getSearchingPlaceHolder().setText(placeholder);
    }

    public void changeApplicationStateToWork(boolean isWorkState) {
        bootstrapper.getRunButton().setEnabled(!isWorkState);
        bootstrapper.getStopButton().setEnabled(isWorkState);
        bootstrapper.getSearchingPlaceHolder().setEnabled(!isWorkState);
        bootstrapper.getSettingsFile().setEnabled(!isWorkState);
        bootstrapper.getInputData().setEnabled(!isWorkState);
    }

    private String cutPath(String path) {
        int size = 120;
        if (path.length() <= size) {
            return path;
        } else {
            return "..."+path.substring(path.length() - (size - 3));
        }
    }

    public void setStatusText(String text) {
        bootstrapper.getLabelStatusData().setText(text);
    }

    public void updateCountItemsStatus(int currentItem, int totalItems) {

        totalItems = totalItems + 1;
        if (totalItems > 1) {
            setStatusText("Processed " + currentItem + "/" + (totalItems) +" items.");
        }
        else {
            setStatusText("Processed " + currentItem + "/" + (totalItems) +" items");
        }
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            Logger.tag("SYSTEM").error(e, "Interrupt exception");
        }
    }
}
