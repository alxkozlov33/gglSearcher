package Services;


import GUI.*;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.io.File;

public class GuiService {
    private Bootstrapper bootstrapper;

    public GuiService() {
        bootstrapper = new Bootstrapper();
        bootstrapper.setTitle("Info searcher v2.6.5 [GGL]");
        bootstrapper.setVisible(true);
        bootstrapper.setResizable(false);
        bootstrapper.setSize(800, 700);
    }

    public String getSearchPlaceholderText(){
        return bootstrapper.getSearchingPlaceHolder().getText();
    }

    public void setSettingsFilePath(String path) {
        bootstrapper.getExceptionsLabelFileData().setText(path);
    }

    private Bootstrapper getBootstrapper() {
        return bootstrapper;
    }

    public void logAction(String message) {
        try {
            bootstrapper.getAutoScrollTextArea().append(message + "\n");
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
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
        //propertiesService.saveWorkState(isWorkState);
        if (!isWorkState) {
           // propertiesService.saveIndex(0);
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
            e.printStackTrace();
        }
    }


}
