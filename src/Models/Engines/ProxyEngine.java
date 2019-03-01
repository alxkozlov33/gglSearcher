package Models.Engines;

import Abstract.WebEngine;
import Models.RequestData;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Connection;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;

public class ProxyEngine extends WebEngine {

    //http://pubproxy.com/api/proxy?google=true&last_check=3&api=ZlBnbzgzUnhvUjBqbytFa1dZTzAzdz09&format=txt
    public Proxy getNewProxy(RequestData requestData) throws IOException {
        Proxy proxy = null;
        Connection.Response response = makeRequest(requestData);
        if (isValidResponse(response)) {
            String responseString = response.parse().text();
            if (!StringUtils.isEmpty(responseString)) {
                proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(responseString.split(":")[0], Integer.parseInt(responseString.split(":")[1])));
            }
        }
        return proxy;
    }
}
