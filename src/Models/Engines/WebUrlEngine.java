package Models.Engines;

import Abstract.WebEngine;
import Models.RequestData;
import com.jcabi.aspects.RetryOnFailure;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import java.io.IOException;

public class WebUrlEngine extends WebEngine {
    private final int requestDelay = 15000;
    public Element getWebSourceData(RequestData requestData) throws IOException {
        Connection.Response response = makeRequest(requestData);
        if(isValidResponse(response)) {
            return response.parse();
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
