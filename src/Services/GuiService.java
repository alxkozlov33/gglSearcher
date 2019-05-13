package Services;

import Abstract.Models.SearchSettings;
import GUI.*;
import Utils.DirUtils;
import org.tinylog.Logger;
import java.awt.*;
import java.io.File;

public class GuiService {
    private static Bootstrapper bootstrapper;
    private static SettingsDialog settingsDialog;

    public GuiService() {
    }

    public Frame getMainFrame() {
        return bootstrapper;
    }

    public SettingsDialog getSettingsDialog() {
        return settingsDialog;
    }

    public void createNewSettingsDialog(DIResolver diResolver) {
        settingsDialog = new SettingsDialog(diResolver);
        settingsDialog.setSize(500, 700);
        settingsDialog.setTitle("Settings");
        settingsDialog.setVisible(true);
        settingsDialog.setResizable(false);
    }

    public void closeSettingsDialog() {
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

    public void clearSettingsFilePath() {
        bootstrapper.getSettingsLabelFileData().setText("");
    }

    public void clearInputDataFilePath() {
        bootstrapper.getSelectedFileLabelData().setText("");
    }

    public void setSettingsFilePath(File file) {
        if (DirUtils.isFileOk(file, "txt")) {
            bootstrapper.getSettingsLabelFileData().setText(cutPath(file.getAbsolutePath()));
        }
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

        if (totalItems > 1) {
            setStatusText("Processed " + currentItem + "/" + (totalItems - 1) +" items.");
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
