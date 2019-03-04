package Models.Engines;

import Abstract.WebEngine;
import Models.RequestData;
import com.jcabi.aspects.RetryOnFailure;
import org.jsoup.Connection;

import java.io.IOException;

public class WebUrlEngine extends WebEngine {
    private final int requestDelay = 15000;
    public void getResultsFromGoogle(RequestData requestData) throws IOException {
        Connection.Response response = makeRequest(requestData);
        if(isValidResponse(response)) {

        }
    }

    @Override
    @RetryOnFailure(delay = requestDelay)
    protected Connection.Response makeRequest(RequestData requestData) throws IOException {
        return null;
    }
}
