package GUI;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class Bootstrapper extends JFrame {
    private JPanel mainPanel;
    private JButton runButton;
    private JLabel labelStatus;
    private JLabel labelStatusData;
    private JButton stopButton;
    private JLabel selectedFileLabelData;
    private JLabel selectedFileLabel;
    private JButton selectFileButton;
    private JTextField searchingPlaceHolder;
    private JPanel logPanel;
    private JPanel logTextPanel;
    private JTextArea logWindow;
    private JScrollBar verticalScroolBar;

    public Bootstrapper() {
        this.setContentPane(mainPanel);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public JLabel getLabelStatusData() {
        return labelStatusData;
    }

    public JButton getRunButton() {
        return runButton;
    }

    public JButton getStopButton() {
        return stopButton;
    }

    public JLabel getSelectedFileLabelData() {
        return selectedFileLabelData;
    }

    public JButton getSelectFileButton() {
        return selectFileButton;
    }

    public JTextArea getLogWindow() {
        return logWindow;
    }

    public JTextField getSearchingPlaceHolder() {
        return searchingPlaceHolder;
    }

    public JPanel getLogPanel() {
        return logPanel;
    }

    public JScrollBar getVerticalScroolBar() {
        return verticalScroolBar;
    }
}

