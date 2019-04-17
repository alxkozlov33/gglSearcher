package Abstract.Engines;

import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.HTMLParserListener;
import com.gargoylesoftware.htmlunit.javascript.JavaScriptErrorListener;
import com.gargoylesoftware.htmlunit.util.WebClientUtils;
import com.gargoylesoftware.htmlunit.util.WebConnectionWrapper;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;

class ProxyWebEngine extends BaseEngine {
    private final HtmlUnitDriver webDriver;

    private ProxyWebEngine() {
        webDriver = new HtmlUnitDriver(BrowserVersion.CHROME, true) {
            @Override
            protected WebClient modifyWebClient(WebClient client) {

                String login = username + "-session-" + new Random().nextInt(Integer.MAX_VALUE);

//                HttpHost super_proxy = new HttpHost(hostName, port);
//                CredentialsProvider cred_provider = new BasicCredentialsProvider();
//                cred_provider.setCredentials(new AuthScope(super_proxy),
//                        new UsernamePasswordCredentials(login, password));
//
//                client.setCredentialsProvider(cred_provider);

                client.waitForBackgroundJavaScript(30000);
                client.waitForBackgroundJavaScriptStartingBefore(30000);
                client.getOptions().setUseInsecureSSL(true);
                client.getOptions().setPrintContentOnFailingStatusCode(true);
                client.setCssErrorHandler(new SilentCssErrorHandler());
                client.setAjaxController(new NicelyResynchronizingAjaxController());
                client.setJavaScriptTimeout(30000);
                client.getOptions().setUseInsecureSSL(true);
                client.getOptions().setCssEnabled(false);
                client.getOptions().setJavaScriptEnabled(true);

                DefaultCredentialsProvider credentialsProvider = new
                        DefaultCredentialsProvider();

                credentialsProvider.addNTLMCredentials(username, password, hostName, port, "work", "domain");
                client.setCredentialsProvider(credentialsProvider);

                shutUpDirtyMouth(client);
                client.setWebConnection(new WebConnectionWrapper(client));

                return client;
            }
        };

        webDriver.navigate().to("http://lumtest.com/myip.json");
        String str = webDriver.getPageSource();
        System.out.println(str);
    }

    private void shutUpDirtyMouth(WebClient webClient) {

        webClient.setIncorrectnessListener((arg0, arg1) -> {
            // TODO Auto-generated method stub
        });

        webClient.setCssErrorHandler(new SilentCssErrorHandler());

        webClient.setJavaScriptErrorListener(new JavaScriptErrorListener() {

            @Override
            public void scriptException(InteractivePage interactivePage, ScriptException e) {

            }

            @Override
            public void timeoutError(InteractivePage interactivePage, long l, long l1) {

            }

            @Override
            public void malformedScriptURL(InteractivePage interactivePage, String s, MalformedURLException e) {

            }

            @Override
            public void loadScriptError(InteractivePage interactivePage, URL url, Exception e) {

            }
        });
        webClient.setHTMLParserListener(new HTMLParserListener() {

            @Override
            public void error(String s, URL url, String s1, int i, int i1, String s2) {

            }

            @Override
            public void warning(String s, URL url, String s1, int i, int i1, String s2) {

            }
        });

        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
    }
}
