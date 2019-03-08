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

    public ProxyService() {
        proxyEngine = new ProxyEngine();
    }

    public Proxy getNewProxyAddress() {
        RequestData requestData = new RequestData(
                "http://pubproxy.com/api/proxy?google=true&last_check=3&api=ZlBnbzgzUnhvUjBqbytFa1dZTzAzdz09&format=txt",
                "DuckDuckBot/1.0; (+http://duckduckgo.com/duckduckbot.html)");
        Connection.Response response;
        String textProxy = null;
        try {
            response = proxyEngine.makeRequest(requestData);
            textProxy = response.parse().text();
        } catch (IOException e) {
            Logger.error(e, "Error while proxy request executing");
        }

        Proxy proxy = null;
            if (!StringUtils.isEmpty(textProxy)) {
                proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(textProxy.split(":")[0], Integer.parseInt(textProxy.split(":")[1])));
            }
        return proxy;
    }
}
