package Abstract.Engines;

import java.io.*;
import Abstract.Models.RequestData;
import Services.DIResolver;
import org.apache.http.client.methods.*;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.tinylog.Logger;

public class ProxyWebClient extends BaseEngine {

    public ProxyWebClient() {
    }

    public synchronized Element requestToSearchEngine(RequestData requestData, DIResolver diResolver) throws IOException {
        for (int i = 1; i <= requestData.attemptsCount; i++) {
            boolean isContinueWork = diResolver.getDbConnectionService().getWorkStatus();
            if (!isContinueWork) {
                return null;
            }
            try {
                Thread.sleep(requestData.requestDelay);
            } catch (InterruptedException e) {
                Logger.tag("SYSTEM").error(e);
            }
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
                Logger.tag("SYSTEM").info("Attempt: " + i);
                Logger.tag("SYSTEM").error("Cannot get page source, waiting for next attempt: " + requestData.requestURL + " \nCause: " + ex.getMessage());
            }
            isThreadSleep(i, requestData);
        }
        throw new IOException("Cannot get source: "+ requestData.requestURL);
    }

    private boolean isValidResponse(CloseableHttpResponse response) throws Exception {
        if (response != null && response.getStatusLine().getStatusCode() == 200) {
            return true;
        }
        throw new Exception();
    }
}