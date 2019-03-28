package Abstract.Engines;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

public class ProxyWebEngine extends BaseEngine {
    final HtmlUnitDriver driver;

    public ProxyWebEngine() {
        driver = new HtmlUnitDriver(BrowserVersion.FIREFOX_38) {
            @Override
            protected WebClient modifyWebClient(WebClient client) {
                String login = username+"-session-" + session_id;

                HttpHost super_proxy = new HttpHost(hostName, port);
                CredentialsProvider cred_provider = new BasicCredentialsProvider();
                cred_provider.setCredentials(new AuthScope(super_proxy),
                        new UsernamePasswordCredentials(login, password));
                client.setCredentialsProvider(cred_provider);
                return client;
            }
        };
    }
//        driver.navigate().to("https://www.google.com/search?q=art+gallery+Paris&pws=0&gl=us&gws_rd=cr&num=30");
//
//    WebElement mapsButton = driver.findElement(By.id("lu_map"));
//        mapsButton.click();
//
//        driver.getPageSource();
}
