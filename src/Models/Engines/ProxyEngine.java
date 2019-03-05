package Models.Engines;

import Abstract.WebEngine;
import Models.RequestData;
import com.jcabi.aspects.RetryOnFailure;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;

public class ProxyEngine extends WebEngine {
    private final int requestDelay = 60000;
    private final int attempts = 50;

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
