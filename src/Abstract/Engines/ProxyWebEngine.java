package Abstract.Engines;

import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.util.WebConnectionWrapper;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.io.IOException;
import java.util.logging.Level;

public class ProxyWebEngine extends BaseEngine {
    public final HtmlUnitDriver webDriver;
    public WebClient webClient;
    public final WebDriverWait wait;

    public ProxyWebEngine() {




        webDriver = new HtmlUnitDriver(BrowserVersion.CHROME, true) {
            @Override
            protected WebClient modifyWebClient(WebClient client) {
                String login = username+"-session-" + session_id;

                HttpHost super_proxy = new HttpHost(hostName, port);
                CredentialsProvider cred_provider = new BasicCredentialsProvider();
                cred_provider.setCredentials(new AuthScope(super_proxy),
                        new UsernamePasswordCredentials(login, password));
                client.setCredentialsProvider(cred_provider);
                client.setCookieManager(new CookieManager());
                client.waitForBackgroundJavaScript(30000);
                client.waitForBackgroundJavaScriptStartingBefore(30000);

                client.getOptions().setUseInsecureSSL(true); //ignore ssl certificate
                client.getOptions().setThrowExceptionOnScriptError(false);
                client.getOptions().setThrowExceptionOnFailingStatusCode(false);
                client.getOptions().setPrintContentOnFailingStatusCode(true);
                client.setCssErrorHandler(new SilentCssErrorHandler());
                client.setAjaxController(new NicelyResynchronizingAjaxController());
                client.setJavaScriptTimeout(30000);
                client.getOptions().setUseInsecureSSL(true);
                client.getOptions().setCssEnabled(true);
                client.getOptions().setJavaScriptEnabled(true);

                client.setWebConnection(new WebConnectionWrapper(client) {
                    @Override
                    public WebResponse getResponse(final WebRequest request) throws IOException {
                        WebResponse response = super.getResponse(request);
                        return response;
                    }
                });

                webClient = client;
                return client;
            }
        };

        wait = new WebDriverWait(webDriver, 40);
    }
}
