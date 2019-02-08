package GUI;

import javax.swing.*;

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
    private AutoScrollTextArea autoScrollTextArea;
    private JPanel StatusInfoPanel;
    private JLabel exceptionsLabelFileData;
    private JButton selectExceptionsFileButton;
    private JScrollBar verticalScroolBar;

    public Bootstrapper() {
        setVisible(false);
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

    public JTextField getSearchingPlaceHolder() {
        return searchingPlaceHolder;
    }

    public JPanel getLogPanel() {
        return logPanel;
    }

    public JScrollBar getVerticalScroolBar() {
        return verticalScroolBar;
    }

    private void createUIComponents() {
        autoScrollTextArea = new AutoScrollTextArea();
        getAutoScrollTextArea().setVisible(true);
        // TODO: place custom component creation code here
    }

    public AutoScrollTextArea getAutoScrollTextArea() {
        return autoScrollTextArea;
    }

    public JButton getSelectExceptionsFileButton() {
        return selectExceptionsFileButton;
    }

    public JLabel getExceptionsLabelFileData() {
        return exceptionsLabelFileData;
    }
}

