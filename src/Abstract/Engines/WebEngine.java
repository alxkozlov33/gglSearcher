package Abstract.Engines;

import Models.RequestData;
import com.jcabi.aspects.RetryOnFailure;
import org.jsoup.Connection;

import java.io.IOException;

abstract class WebEngine {

    public boolean isValidResponse(Connection.Response response) {
        boolean isValidFlag = false;
        if (response.statusCode() == 200) {
            isValidFlag = true;
        }
        return isValidFlag;
    }

    protected abstract Connection.Response makeRequest(RequestData requestData) throws IOException;
}
