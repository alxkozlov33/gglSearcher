package Abstract;

import Models.Engines.ProxyEngine;
import Models.RequestData;
import Services.UserAgentsRotatorService;
import com.jcabi.aspects.RetryOnFailure;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;

public abstract class WebEngine {
    private final ProxyEngine proxyEngine;
    private final UserAgentsRotatorService userAgentsRotatorService;

    private final int requestDelay = 15000;

    public WebEngine() {
        proxyEngine = new ProxyEngine();
        userAgentsRotatorService = new UserAgentsRotatorService();
    }

    protected boolean isValidResponse(Connection.Response response) {
        boolean isValidFlag = false;
        if (response.statusCode() == 200) {
            isValidFlag = true;
        }
        return isValidFlag;
    }

    @RetryOnFailure(delay = requestDelay)
    protected Connection.Response makeRequest(RequestData requestData) throws IOException {
        return Jsoup.connect(requestData.requestURL)
                .followRedirects(true)
                .proxy(proxyEngine.getNewProxy(new RequestData("http://pubproxy.com/api/proxy?google=true&last_check=3&api=ZlBnbzgzUnhvUjBqbytFa1dZTzAzdz09&format=txt")))
                .userAgent(userAgentsRotatorService.getRandomUserAgent())
                .method(Connection.Method.GET)
                .ignoreHttpErrors(true)
                .execute();
    }


}
