package Abstract.Strategies.EngineResultsInterpreters.BusinessListResultsProcessing;

import Abstract.Engines.ProxyWebEngine;
import Abstract.Models.SearchResultModels.BusinessListSearchResultItem;
import Utils.StrUtils;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.tinylog.Logger;

import java.util.ArrayList;
import java.util.List;

// "All results" page
public class BusinessResultsStrategyTypeOne extends BusinessResultItemsProcess {

    @Override
    public List<BusinessListSearchResultItem> processBody(ProxyWebEngine proxyWebEngine) {
        List results = new ArrayList<>();
        try {
            WebElement mapButton = proxyWebEngine.webDriver.findElement(By.cssSelector("#lu_map"));
            mapButton.click();
            results = processMapsPage(proxyWebEngine);
        } catch (InterruptedException e) {
            Logger.error(e);
        }
        return results;
    }
}
