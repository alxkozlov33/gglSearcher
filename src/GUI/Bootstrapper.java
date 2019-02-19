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
    private JMenuBar menubar;

    private JMenu exceptionsFile;
    private JMenu inputData;

    private JMenuItem clearExceptionsFile;
    private JMenuItem chooseExceptionsFile;

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
        chooseExceptionsFile = new JMenuItem("Choose new exceptions file");
        getExceptionsFile().add(getChooseExceptionsFile());
        clearExceptionsFile = new JMenuItem("Clear exceptions file");
        getExceptionsFile().add(getClearExceptionsFile());

        getMenubar().add(getInputData());
        getMenubar().add(getExceptionsFile());
        setJMenuBar(getMenubar());
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

    public JMenuItem getClearExceptionsFile() {
        return clearExceptionsFile;
    }

    public JMenuItem getChooseExceptionsFile() {
        return chooseExceptionsFile;
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

