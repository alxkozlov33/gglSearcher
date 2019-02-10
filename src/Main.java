import GUI.Bootstrapper;

import javax.swing.*;

public class Main {

    private Bootstrapper gui;

    public static void main(String[] args) {
        Main main = new Main();
        main.start();
    }

    public void start() {
        initLookAndFeel();
        initGUI();

        new DIResolver().initDependencies(gui);
    }

    private void initGUI() {
        gui = new Bootstrapper();
        gui.setTitle("Info searcher v1.4 [DDG]");
        gui.setVisible(true);
        gui.setResizable(false);
        gui.setSize(800, 700);
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
