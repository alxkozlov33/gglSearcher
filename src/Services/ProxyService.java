package Services;

import Models.Engines.ProxyEngine;
import Models.RequestData;
import Utils.StrUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;

class ProxyService {
    private LogService logService;

    ProxyEngine proxyEngine = new ProxyEngine();


    ProxyService(LogService logService) {
        this.logService = logService;
    }
//    Proxy getNewProxy() {
//        Connection.Response response = null;
//        String json = null;
//
//        boolean success = false;
//
//        while(!success) {
//            try {
//                response = Jsoup.connect("http://pubproxy.com/api/proxy?google=true&last_check=3&api=ZlBnbzgzUnhvUjBqbytFa1dZTzAzdz09&format=txt")
//                        .ignoreContentType(true)
//                        .userAgent("DuckDuckBot/1.0; (+http://duckduckgo.com/duckduckbot.html)")
//                        .method(Connection.Method.GET)
//                        .ignoreHttpErrors(true)
//                        .timeout(10000)
//                        .execute();
//
//                json = response.parse().text();
//                logService.LogMessage("Response from proxy service: " + json);
//                if (StrUtils.isProxyGrabbed(json)) {
//                    success = true;
//                    break;
//                }
//            } catch (IOException e) {
//                logService.LogMessage("Cannot get proxy");
//            }
//            try {
//                logService.LogMessage("Waiting 5 minutes to get proxy again...");
//                Thread.sleep(300000);
//            } catch (InterruptedException e) {
//                logService.LogMessage("Thread sleep 300000 problem");
//            }
//            success = false;
//        }
//
//        Proxy proxy = null;
//        if (success) {
//            if (!StringUtils.isEmpty(json)) {
//                proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(json.split(":")[0], Integer.parseInt(json.split(":")[1])));
//            }
//        }
//        return proxy;
//    }


    public Proxy getNewProxyAddress() throws IOException {
        RequestData requestData = new RequestData(
                "http://pubproxy.com/api/proxy?google=true&last_check=3&api=ZlBnbzgzUnhvUjBqbytFa1dZTzAzdz09&format=txt",
                "DuckDuckBot/1.0; (+http://duckduckgo.com/duckduckbot.html)");
        Connection.Response response = proxyEngine.makeRequest(requestData);
        String textProxy = response.parse().text();

        Proxy proxy = null;
            if (!StringUtils.isEmpty(textProxy)) {
                proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(textProxy.split(":")[0], Integer.parseInt(textProxy.split(":")[1])));
            }
        return proxy;
    }
}
