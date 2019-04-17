package Abstract.Strategies.EngineResultsInterpreters.BusinessListResultsProcessing;

import Abstract.Engines.ProxyWebClient;
import Abstract.Models.InputModels.InputCsvModelItem;
import Abstract.Models.RequestData;
import Abstract.Models.SearchResultModels.BusinessListSearchResultItem;
import Utils.StrUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.tinylog.Logger;
import us.codecraft.xsoup.Xsoup;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class BusinessResultItemsProcess {

    public BusinessResultItemsProcess() {
    }

    public List<BusinessListSearchResultItem> processData(Element body, InputCsvModelItem inputCsvModelItem, ProxyWebClient proxyWebClient) {

        List<BusinessListSearchResultItem> results = new ArrayList<>();
        String linkToMaps;
        Element mapsPage;
        try {
            linkToMaps = Jsoup.parse(Xsoup.compile("//*[@id=\"ires\"]/ol/div[1]/a").evaluate(body).get()).body().select("a").attr("href");
        } catch(Exception ex) {
            Logger.error(ex, "Cannot locate map in google results");
            return results;
        }
        mapsPage = getMapsPage(linkToMaps);

        URL nextButton;
        AdditionalBusinessRequestToGoogle additionalBusinessRequestToGoogle = new AdditionalBusinessRequestToGoogle();
        do {
            nextButton = getNextButton(mapsPage);
            int placesSize = mapsPage.select("a.rllt__link").size();
            for (int i = 1; i <= placesSize; i++) {
                String placeName = Xsoup.compile("//*[@id=\"rl_ist0\"]/div[1]/div[4]/div[" + i + "]/div/div[3]/div/a/div/div[2]/div").evaluate(mapsPage).get();
                BusinessListSearchResultItem regularSearchResultItem = new BusinessListSearchResultItem(placeName, "", "", inputCsvModelItem);
                additionalBusinessRequestToGoogle.processData(regularSearchResultItem);
                results.add(regularSearchResultItem);
            }

            if (nextButton != null) {
                mapsPage = getMapsPage(linkToMaps);
            }
        } while (nextButton != null);
        return results;
    }

    private Element getMapsPage(String linkToMaps) {
        Element mapsPage = null;
        try {
            mapsPage = new ProxyWebClient().request(new RequestData(StrUtils.createUrlForGoogleMaps(linkToMaps), 500, 5000));
        } catch (IOException ex) {
            Logger.error(ex);
        }
        return mapsPage;
    }


    private URL getNextButton(Element body) {
        URL nextButton = null;
        try {
            nextButton = new URL(body.select("#pnnext").attr("href"));
        } catch (MalformedURLException e) {
            Logger.error(e);
        }
        return nextButton;
    }
}
