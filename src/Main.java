import Abstract.Commands.ApplicationStartedActionCommand;
import GUI.Bootstrapper;
import GUI.Browser;
import Services.*;
import chrriis.common.UIUtils;
import chrriis.dj.nativeswing.swtimpl.NativeInterface;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.logging.Level;

public class Main {
    public static void main(String[] args) {
        Main main = new Main();
        main.start();
    }

    private void start() {
        initLookAndFeel();

        tests();
        java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(Level.OFF);
        java.util.logging.Logger.getLogger("org.apache.commons.httpclient").setLevel(Level.OFF);
        System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");

        GuiService guiService = new GuiService();
        UserAgentsRotatorService userAgentsRotatorService = new UserAgentsRotatorService();
        PropertiesService propertiesService = new PropertiesService();
        OutputDataService outputDataService = new OutputDataService();
        InputDataService inputDataService = new InputDataService();
        SettingsService settingsService = new SettingsService();

        DIResolver diResolver = new DIResolver(userAgentsRotatorService, propertiesService, guiService, outputDataService, inputDataService, settingsService);

        Bootstrapper bootstrapper = new Bootstrapper(diResolver);
        bootstrapper.setTitle("Info searcher v3.3.2 [GGL]");
        bootstrapper.setVisible(true);
        bootstrapper.setResizable(false);
        bootstrapper.setSize(800, 700);

        guiService.setBootstrapper(bootstrapper);

        ApplicationStartedActionCommand applicationStartedActionCommand = new ApplicationStartedActionCommand(diResolver);
        applicationStartedActionCommand.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
    }

    private void tests() {
//        Browser browser = new Browser();
//        browser.setTitle("Info searcher v3.3.2 [GGL] Browser");
//        browser.setVisible(true);
//        browser.setResizable(false);
//        browser.setSize(800, 700);
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
