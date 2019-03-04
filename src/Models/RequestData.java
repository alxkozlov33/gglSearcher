package Models;

import java.net.Proxy;

public class RequestData {
    public String requestURL;
    public String userAgent;
    public Proxy proxy;

    public RequestData(String requestURL, String userAgent) {
        this.requestURL = requestURL;
        this.userAgent = userAgent;
    }
}
