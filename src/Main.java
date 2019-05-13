import Abstract.Commands.ApplicationStartedActionCommand;
import GUI.Bootstrapper;
import GUI.SettingsDialog;
import Services.*;
import javax.swing.*;
import java.awt.event.ActionEvent;

public class Main {
    public static void main(String[] args) {
        Main main = new Main();
        main.start();
    }

    private void start() {
        initLookAndFeel();

        GuiService guiService = new GuiService();
        UserAgentsRotatorService userAgentsRotatorService = new UserAgentsRotatorService();
        PropertiesService propertiesService = new PropertiesService();
        OutputDataService outputDataService = new OutputDataService();
        InputDataService inputDataService = new InputDataService();
        SettingsService settingsService = new SettingsService();
        DBConnectionService dbConnectionService = new DBConnectionService();

        DIResolver diResolver = new DIResolver(userAgentsRotatorService, propertiesService, guiService, outputDataService, inputDataService, settingsService, dbConnectionService);

        Bootstrapper bootstrapper = new Bootstrapper(diResolver);
        bootstrapper.setTitle("Info searcher v3.4.2 [GGL]");
        bootstrapper.setVisible(true);
        bootstrapper.setResizable(false);
        bootstrapper.setSize(800, 700);

        guiService.setBootstrapper(bootstrapper);

        ApplicationStartedActionCommand applicationStartedActionCommand = new ApplicationStartedActionCommand(diResolver);
        applicationStartedActionCommand.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
    }

    private void initLookAndFeel() {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // If Nimbus is not available, you can set the GUI to another look and feel.
        }
    }
}
