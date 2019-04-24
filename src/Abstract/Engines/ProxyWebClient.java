package Abstract.Engines;

import java.io.*;
import java.util.Random;

import Abstract.Models.RequestData;
import org.apache.http.HttpHost;
import org.apache.http.auth.*;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.*;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.tinylog.Logger;

public class ProxyWebClient extends BaseEngine {

    private CloseableHttpClient getNewClient() {
        String login = username + "-session-" + new Random().nextInt(Integer.MAX_VALUE);

        HttpHost super_proxy = new HttpHost(hostName, port);
        CredentialsProvider cred_provider = new BasicCredentialsProvider();
        cred_provider.setCredentials(new AuthScope(super_proxy),
                new UsernamePasswordCredentials(login, password));

        return HttpClients.custom()
                .setConnectionManager(new BasicHttpClientConnectionManager())
                .setProxy(super_proxy)
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setCookieSpec(CookieSpecs.IGNORE_COOKIES).build())
                .setDefaultCredentialsProvider(cred_provider)
                .build();
    }

    public ProxyWebClient() {

    }

    public synchronized Element request(RequestData requestData) throws IOException {
        for (int i = 1; i <= requestData.attemptsCount; i++) {
//            boolean isContinueWork = diResolver.getPropertiesService().getWorkState();
//            if (!isContinueWork) {
//                return null;
//            }
            HttpGet request = new HttpGet(requestData.requestURL);
            CloseableHttpResponse response = getNewClient().execute(request);
            try {
                if (isValidResponse(response)) {
                    Logger.info("Response OK from: " + requestData.requestURL);
                    String pageSource = EntityUtils.toString(response.getEntity());
                    response.close();
                    return Jsoup.parse(pageSource);
                }
            } catch (Exception ex) {
                Logger.info("Attempt: " + i);
                Logger.error("Cannot get page source, waiting for next attempt: " + requestData.requestURL + " \nCause: " + ex.getMessage());
            }
            isThreadSleep(i, requestData);
        }
        throw new IOException("Cannot get source: "+ requestData.requestURL);
    }

    private void isThreadSleep(int currentAttempt, RequestData requestData) {
        try {
            if (currentAttempt <= requestData.attemptsCount) {
                Thread.sleep(requestData.requestDelay);
            }
        } catch (InterruptedException e) {
            Logger.error("Interrupt exception");
        }
    }

    private boolean isValidResponse(CloseableHttpResponse response) throws Exception {
        if (response != null && response.getStatusLine().getStatusCode() == 200) {
            return true;
        }
        throw new Exception();
    }

    void close() throws IOException {
        client.close();
    }
}