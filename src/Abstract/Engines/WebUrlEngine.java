package Abstract.Engines;

import Models.RequestData;
import com.jcabi.aspects.RetryOnFailure;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.tinylog.Logger;

import java.io.IOException;

public class WebUrlEngine extends WebEngine {
    private final int requestDelay = 15000;
    public Element getWebSourceData(RequestData requestData) {
        Connection.Response response = null;
        try {
            response = makeRequest(requestData);
            if (isValidResponse(response)) {
                return response.parse();
            }
        } catch (IOException e) {
            Logger.tag("SYSTEM").error(e, "Error while request executing");
        }
        return null;
    }

    @Override
    @RetryOnFailure(delay = requestDelay)
    protected Connection.Response makeRequest(RequestData requestData) throws IOException {
        return Jsoup.connect(requestData.requestURL)
                .followRedirects(true)
                .userAgent(requestData.userAgent)
                .proxy(requestData.proxy)
                .method(Connection.Method.GET)
                .ignoreHttpErrors(true)
                .timeout(requestDelay)
                .execute();
    }
}
