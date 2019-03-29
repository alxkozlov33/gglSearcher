package Abstract.Strategies.EngineResultsInterpreters.BusinessListResultsProcessing;

import Abstract.Engines.ProxyWebEngine;
import Abstract.Models.SearchResultModels.BusinessListSearchResultItem;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.tinylog.Logger;
import java.util.ArrayList;
import java.util.List;

//Regular simple page
public class BusinessResultsStrategyTypeTwo extends BusinessResultItemsProcess {

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
