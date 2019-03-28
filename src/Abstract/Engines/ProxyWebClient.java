package Abstract.Engines;

import java.io.*;
import java.util.Random;
import Abstract.Models.RequestData;
import Services.DIResolver;
import org.apache.http.HttpHost;
import org.apache.http.auth.*;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.*;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.tinylog.Logger;

public class ProxyWebClient extends BaseEngine {

    private DIResolver diResolver;

    public ProxyWebClient(DIResolver diResolver) {
        this.diResolver = diResolver;
        String login = username + "-session-" + session_id;

        HttpHost super_proxy = new HttpHost(hostName, port);
        CredentialsProvider cred_provider = new BasicCredentialsProvider();
        cred_provider.setCredentials(new AuthScope(super_proxy),
                new UsernamePasswordCredentials(login, password));
        client = HttpClients.custom()
                .setConnectionManager(new BasicHttpClientConnectionManager())
                .setProxy(super_proxy)
                .setDefaultCredentialsProvider(cred_provider)
                .build();
    }

    public synchronized Element request(RequestData requestData) {
        for (int i = 1; i <= requestData.attemptsCount; i++) {
            boolean isContinueWork = diResolver.getPropertiesService().getWorkState();
            if (!isContinueWork) {
                return null;
            }
            HttpGet request = new HttpGet(requestData.requestURL);
            try (CloseableHttpResponse response = client.execute(request)) {
                if (isValidResponse(response)) {
                    Logger.info("Response OK from: " + requestData.requestURL);
                    return Jsoup.parse(EntityUtils.toString(response.getEntity()));
                }
                throw new Exception();
            } catch (Exception ex) {
                Logger.info("Attempt: " + i);
                Logger.error("Cannot get page source, waiting for next attempt: " + requestData.requestURL + " \nCause: " + ex.getMessage());
            }
            isThreadSleep(i, requestData);
        }
        return null;
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

    private boolean isValidResponse(CloseableHttpResponse response) {
        return response != null && response.getStatusLine().getStatusCode() == 200;
    }

    void close() throws IOException {
        client.close();
    }
}