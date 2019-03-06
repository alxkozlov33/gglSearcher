package GUI;

import Abstract.Commands.*;
import Services.DIResolver;

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
    private JMenuBar menubar;

    private JMenu exceptionsFile;
    private JMenu inputData;

    private JMenuItem clearSettingsFile;
    private JMenuItem chooseSettingsFile;

    private JMenuItem chooseInputFile;
    private JMenuItem clearInputFile;

    public Bootstrapper() {
        setVisible(false);
        this.setContentPane(mainPanel);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        inputData = new JMenu("Data file");
        chooseInputFile = new JMenuItem("Choose new data file");
        getInputData().add(getChooseInputFile());
        clearInputFile = new JMenuItem("Clear existing file");
        getInputData().add(getClearInputFile());

        exceptionsFile = new JMenu("Exceptions file");
        chooseSettingsFile = new JMenuItem("Choose new settings file");
        getExceptionsFile().add(getChooseSettingsFile());
        clearSettingsFile = new JMenuItem("Clear settings file");
        getExceptionsFile().add(getClearSettingsFile());

        getMenubar().add(getInputData());
        getMenubar().add(getExceptionsFile());
        setJMenuBar(getMenubar());


        DIResolver diResolver = new DIResolver();
        runButton.setAction(new RunButtonActionCommand(diResolver));
        stopButton.setAction(new StopButtonActionCommand(diResolver));

        chooseSettingsFile.setAction(new SelectSettingsFileActionCommand(diResolver));
        clearSettingsFile.setAction(new ClearSettingsFileActionCommand(diResolver));

        chooseInputFile.setAction(new SelectInputDataFileActionCommand(diResolver));
        clearInputFile.setAction(new ClearInputDataFileActionCommand(diResolver));
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

    public JMenuBar getMenubar() {
        if (menubar == null) {
            menubar = new JMenuBar();
        }
        return menubar;
    }

    public JMenuItem getClearSettingsFile() {
        return clearSettingsFile;
    }

    public JMenuItem getChooseSettingsFile() {
        return chooseSettingsFile;
    }

    public JMenuItem getChooseInputFile() {
        return chooseInputFile;
    }

    public JMenuItem getClearInputFile() {
        return clearInputFile;
    }

    public JMenu getExceptionsFile() {
        return exceptionsFile;
    }

    public JMenu getInputData() {
        return inputData;
    }
}

