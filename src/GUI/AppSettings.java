package GUI;

import Services.DBConnectionService;
import Services.DIResolver;

import javax.swing.*;
import java.awt.event.*;

public class AppSettings extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JCheckBox searchEngine;
    private JCheckBox mapsEngine;
    private final DBConnectionService dbConnectionService;

    public AppSettings(DIResolver diResolver) {
        this.dbConnectionService = diResolver.getDbConnectionService();
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        setResizable(false);

        buttonOK.addActionListener(e -> onOK());

        buttonCancel.addActionListener(e -> onCancel());

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        fillGuiDataFromDatabase();
    }

    private void onOK() {
        // add your code here
        this.dbConnectionService.updateGoogleSearchEngine(String.valueOf(searchEngine.isSelected()));
        this.dbConnectionService.updateGoogleMapsEngine(String.valueOf(mapsEngine.isSelected()));
        dispose();
    }

    private void fillGuiDataFromDatabase() {
        this.searchEngine.setSelected(this.dbConnectionService.getGoogleSearchEngine());
        this.mapsEngine.setSelected(this.dbConnectionService.getGoogleMapsEngine());
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public void Dispose() {
        dispose();
    }
}
