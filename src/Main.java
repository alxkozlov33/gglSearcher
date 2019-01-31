import Services.ServicesManager;

import javax.swing.*;

public class Main {

    private Bootstrapper gui;
    private ServicesManager serviceManager;
    private MainWorker mainWorker;

    public static void main(String[] args) {
        Main main = new Main();
        main.start();
    }

    public void start() {
        initLookAndFeel();
        initGUI();
        initLogic();
        initServices();
    }

    private void initGUI() {
        gui = new Bootstrapper();
        gui.setTitle("Google searcher v1.0");
        gui.setVisible(true);
        gui.setResizable(false);
        gui.setSize(500, 700);
    }

    private void initLogic() {
        mainWorker = new MainWorker();
    }

    private void initServices() {
        serviceManager = new ServicesManager();
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
