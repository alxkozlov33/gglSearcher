package Abstract.Engines;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import java.util.concurrent.TimeUnit;

public class ProxyWebEngine extends BaseEngine {
    public final HtmlUnitDriver webDriver;

    public ProxyWebEngine() {
        webDriver = new HtmlUnitDriver(BrowserVersion.FIREFOX_38) {
            @Override
            protected WebClient modifyWebClient(WebClient client) {
                String login = username+"-session-" + session_id;

                HttpHost super_proxy = new HttpHost(hostName, port);
                CredentialsProvider cred_provider = new BasicCredentialsProvider();
                cred_provider.setCredentials(new AuthScope(super_proxy),
                        new UsernamePasswordCredentials(login, password));
                client.setCredentialsProvider(cred_provider);

                client.waitForBackgroundJavaScript(30000);
                client.waitForBackgroundJavaScriptStartingBefore(30000);

                return client;
            }
        };
        webDriver.setJavascriptEnabled(true);
        webDriver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    }
}
