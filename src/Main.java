import GUI.Bootstrapper;
import Services.DIResolver;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        Main main = new Main();
        main.start();
    }

    public void start() {
        initLookAndFeel();

        new DIResolver().initDependencies();
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
