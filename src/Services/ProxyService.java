package Services;

import Abstract.Engines.ProxyEngine;
import Models.RequestData;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Connection;
import org.tinylog.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;

public class ProxyService {
    private ProxyEngine proxyEngine;
    private final int attempts = 20;

    public ProxyService() {
        proxyEngine = new ProxyEngine();
    }

    public Proxy getNewProxyAddress() {
        Proxy proxy = null;
        for (int i = 1; i <= attempts; i++) {
            try {
                RequestData requestData = new RequestData(
                        "http://pubproxy.com/api/proxy?google=true&last_check=3&api=ZlBnbzgzUnhvUjBqbytFa1dZTzAzdz09&format=txt",
                        "DuckDuckBot/1.0; (+http://duckduckgo.com/duckduckbot.html)");

                Connection.Response response = proxyEngine.makeRequest(requestData);
                String textProxy = response.parse().text();
                if (!StringUtils.isEmpty(textProxy)) {
                    proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(textProxy.split(":")[0], Integer.parseInt(textProxy.split(":")[1])));
                }
            } catch (IOException e) {
                Logger.tag("SYSTEM").error(e, "Error while proxy request executing, waiting for next attempt");
            }
            isThreadSleep(i);
        }
        return proxy;
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
}
