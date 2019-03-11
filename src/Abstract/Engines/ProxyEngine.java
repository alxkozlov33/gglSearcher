package Abstract.Engines;

import Models.RequestData;
import com.jcabi.aspects.RetryOnFailure;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.tinylog.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;

public class ProxyEngine extends WebEngine {

    private final int requestDelay = 5000;
    private final int attempts = 50;

    Proxy getNewProxy() {
        RequestData requestData = new RequestData(
                "http://pubproxy.com/api/proxy?google=true&last_check=3&api=ZlBnbzgzUnhvUjBqbytFa1dZTzAzdz09&format=txt",
                "DuckDuckBot/1.0; (+http://duckduckgo.com/duckduckbot.html)");
        for (int i = 1; i <= attempts; i++) {
            try {
                Connection.Response response = makeRequest(requestData);
                if (isValidResponse(response)) {
                    String textProxy = response.parse().text();
                    if (!StringUtils.isEmpty(textProxy)) {
                        return new Proxy(Proxy.Type.HTTP, new InetSocketAddress(textProxy.split(":")[0], Integer.parseInt(textProxy.split(":")[1])));
                    }
                }
            } catch (Exception ex) {
                Logger.tag("SYSTEM").error("Cannot get proxy, waiting for next attempt");
                Logger.tag("SYSTEM").error(ex.getMessage());
            }
            isThreadSleep(i);
        }
        return null;
    }

    private void isThreadSleep(int currentAttempt) {
        try {
            if (currentAttempt <= attempts) {
                Thread.sleep(requestDelay);
            }
        } catch (InterruptedException e) {
            Logger.tag("SYSTEM").error("Interrupt exception");
        }
    }

    @Override
    @RetryOnFailure(delay = requestDelay, attempts = attempts)
    public Connection.Response makeRequest(RequestData requestData) throws IOException {
        return Jsoup.connect(requestData.requestURL)
                .followRedirects(true)
                .userAgent(requestData.userAgent)
                .method(Connection.Method.GET)
                .ignoreHttpErrors(true)
                .timeout(requestDelay)
                .execute();
    }
}
