package Abstract.Engines;

import Abstract.Models.RequestData;
import Services.DIResolver;
import kbaa.gsearch.PlaceCard;
import kbaa.gsearch.Search;
import org.tinylog.Logger;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CustomProxyMapsClient extends BaseEngine {

    public CustomProxyMapsClient() {
    }

    public synchronized List<PlaceCard> requestToMapsEngine(RequestData requestData, DIResolver diResolver) throws IOException {
        List<PlaceCard> placeCards = new ArrayList<>();
        for (int i = 1; i <= requestData.attemptsCount; i++) {
            boolean isContinueWork = diResolver.getDbConnectionService().getWorkStatus();
            if (!isContinueWork) {
                return placeCards;
            }

            Search search = new Search(requestData.getRequestTerm(), getNewClient());
            search.setUserAgent(diResolver.getUserAgentsRotatorService().getUserAgent());
            try {
                Thread.sleep(requestData.requestDelay);
                search.perform();
                do {
                    Logger.tag("SYSTEM").info("Response OK from: " + requestData.requestURL);
                    placeCards.addAll(search.getResults());
                    i++;
                    if (search.hasNextPage()){
                        try {
                            search.loadNextPage();
                        } catch (Exception e) {
                            Logger.tag("SYSTEM").error("Cannot get page of places list");
                        }
                    } else {
                        break;
                    }
                } while (i < 20);
                return placeCards;
            } catch (Search.IncorrectPageException ex) {
                Logger.tag("SYSTEM").error("HTML with problems. \n");
                //Logger.error(ex.getPageSource());
                return placeCards;
            } catch (Exception e) {
                Logger.tag("SYSTEM").error("Attempt: " + i +" failed. Next attempt in: "+((requestData.requestDelay * 2)/1000)+"s. " +
                        "\nCannot get maps engine page source, waiting for next attempt: " + requestData.requestURL + " " +
                        "\nCause: " + e);
            }
            isThreadSleep(i, requestData);
        }
        throw new IOException("Cannot get maps source: "+ requestData.requestURL);
    }
}
