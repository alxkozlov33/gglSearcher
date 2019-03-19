import Abstract.Commands.ApplicationStartedActionCommand;
import GUI.Bootstrapper;
import Services.*;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class Main {

    public static void main(String[] args) {
        Main main = new Main();
        main.start();
    }

    public void start() {
        initLookAndFeel();

        GuiService guiService = new GuiService();
        UserAgentsRotatorService userAgentsRotatorService = new UserAgentsRotatorService();
        PropertiesService propertiesService = new PropertiesService();
        OutputDataService outputDataService = new OutputDataService();
        InputDataService inputDataService = new InputDataService();
        SettingsService settingsService = new SettingsService();

        DIResolver diResolver = new DIResolver(userAgentsRotatorService, propertiesService, guiService, outputDataService, inputDataService, settingsService);

        Bootstrapper bootstrapper = new Bootstrapper(diResolver);
        bootstrapper.setTitle("Info searcher v3.3 [GGL]");
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
