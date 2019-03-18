package Abstract.Engines;

import Abstract.Models.RequestData;
import org.jsoup.Connection;
import java.io.IOException;

abstract class WebEngine {

    boolean isWorkInterrupted = true;
    final int requestDelay = 5000;
    final int attempts = 50;

    protected void interruptWork() {
        isWorkInterrupted = false;
    }

    boolean isValidResponse(Connection.Response response) {
        return response != null && response.statusCode() == 200;
    }

    protected abstract Connection.Response makeRequest(RequestData requestData) throws IOException;
}
