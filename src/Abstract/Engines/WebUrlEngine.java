package Abstract.Engines;

import Models.RequestData;
import Services.ProxyService;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.tinylog.Logger;
import java.io.IOException;

public class WebUrlEngine extends WebEngine {
    private final ProxyService proxyService;
    private final int attempts = 5;

    public WebUrlEngine() {
        this.proxyService = new ProxyService();
    }

    public Element getWebSourceData(RequestData requestData) {
        for (int i = 1; i <= attempts; i++) {
            try {
                Connection.Response response = makeRequest(requestData);
                if (isValidResponse(response)) {
                    return response.parse();
                }
            } catch (Exception ex) {
                Logger.tag("SYSTEM").error("Cannot get page source, waiting for next attempt: " + requestData.requestURL);
                Logger.tag("SYSTEM").error(ex.getMessage());
            }
            isThreadSleep(i);
        }
        return null;
    }

    private void isThreadSleep(int currentAttempt) {
        try {
            if (currentAttempt <= attempts) {
                Thread.sleep(5000);
            }
        } catch (InterruptedException e) {
            Logger.tag("SYSTEM").error("Interrupt exception");
        }
    }

    @Override
    protected Connection.Response makeRequest(RequestData requestData) throws IOException {
        return Jsoup.connect(requestData.requestURL)
                .followRedirects(true)
                .userAgent(requestData.userAgent)
                .proxy(proxyService.getNewProxyAddress())
                .method(Connection.Method.GET)
                .ignoreHttpErrors(true)
                .timeout(60 * 1000)
                .execute();
    }
}
