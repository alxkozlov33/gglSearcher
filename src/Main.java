import Abstract.Commands.ApplicationStartedActionCommand;
import GUI.Bootstrapper;
import Services.*;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.DefaultCredentialsProvider;
import com.gargoylesoftware.htmlunit.ProxyConfig;
import com.gargoylesoftware.htmlunit.WebClient;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.openqa.selenium.By;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.Random;

public class Main {

    public static final int port = 22225;
    public static final String username = "lum-customer-CUSTOMER-zone-YOURZONE";
    public static final String password = "YOURPASS";
    public String session_id = Integer.toString(new Random().nextInt(Integer.MAX_VALUE));

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
        bootstrapper.setTitle("Info searcher v3.3.2 [GGL]");
        bootstrapper.setVisible(true);
        bootstrapper.setResizable(false);
        bootstrapper.setSize(800, 700);

        guiService.setBootstrapper(bootstrapper);

        ApplicationStartedActionCommand applicationStartedActionCommand = new ApplicationStartedActionCommand(diResolver);
        applicationStartedActionCommand.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));

        //"https://www.google.com/search?q=%22art+gallery%22+York+England&pws=0&gl=us&gws_rd=cr&num=30"
        HtmlUnitDriver driver = new HtmlUnitDriver(BrowserVersion.FIREFOX_38) {
            @Override
            protected WebClient modifyWebClient(WebClient client) {
                String login = username+"-session-" + session_id;

                HttpHost super_proxy = new HttpHost("zproxy.luminati.io", port);
                CredentialsProvider cred_provider = new BasicCredentialsProvider();
                cred_provider.setCredentials(new AuthScope(super_proxy),
                        new UsernamePasswordCredentials(login, password));
                client.setCredentialsProvider(cred_provider);
                return client;
            }
        };

        driver.setJavascriptEnabled(true);
        driver.navigate().to("https://www.google.com/search?q=art+gallery+Paris&pws=0&gl=us&gws_rd=cr&num=30");

        WebElement mapsButton = driver.findElement(By.id("lu_map"));
        mapsButton.click();

        HttpHost super_proxy = new HttpHost("zproxy.luminati.io", port);
        CredentialsProvider cred_provider = new BasicCredentialsProvider();
        cred_provider.setCredentials(new AuthScope(super_proxy),
                new UsernamePasswordCredentials(login, password));
        ProxyConfig proxyConfig = new ProxyConfig("zproxy.luminati.io", port);

        driver.setAutoProxy("");
        Proxy proxy = new Proxy();

        driver.getPageSource();



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
