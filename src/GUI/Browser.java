package GUI;

import chrriis.dj.nativeswing.swtimpl.components.JWebBrowser;

import javax.swing.*;
import java.awt.*;

public class Browser extends JFrame {
    private JPanel mainBrowserPanel;

    public Browser() {
        setVisible(true);
        this.setContentPane(mainBrowserPanel);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        final JWebBrowser jWebBrowser = new JWebBrowser();
        jWebBrowser.navigate("http://www.google.com/");
        mainBrowserPanel.add(jWebBrowser, BorderLayout.CENTER);
    }
}
