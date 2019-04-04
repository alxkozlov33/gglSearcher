package Abstract.Strategies.EngineResultsInterpreters.BusinessListResultsProcessing;

import Abstract.Engines.ProxyWebEngine;
import Abstract.Models.SearchResultModels.BusinessListSearchResultItem;
import org.openqa.selenium.*;
import org.tinylog.Logger;
import java.util.ArrayList;
import java.util.List;

public class BusinessResultItemsProcess {
    private ProxyWebEngine proxyWebEngine;

    public List<BusinessListSearchResultItem> processBody(ProxyWebEngine proxyWebEngine) {
        this.proxyWebEngine = proxyWebEngine;

        List results = new ArrayList<>();
        WebElement linkToMaps = proxyWebEngine.webDriver.findElementByXPath("//*[@id=\"rso\"]/div[1]/div/div/div[2]/div/div[1]/a");
        linkToMaps.click();

        WebElement nextButton = null;
        try {
            nextButton = proxyWebEngine.webDriver.findElement(By.id("pnnext"));
        } catch(NoSuchElementException ex) {
            Logger.error(ex);
        }
        do {
            List<WebElement> elements = proxyWebEngine.webDriver.findElements((By.cssSelector("a.rllt__link")));
            for (WebElement item : elements) {
                String galleryName = item.getText();
                //TODO: There we need to make additional request
            }
            if (nextButton != null) {
                nextButton.click();
            }
            nextButton = getNextButton();

            //BusinessListSearchResultItem regularSearchResultItem = new BusinessListSearchResultItem(galleryName, webSite, "", address, StrUtils.getCountryFromAddress(address));
            //results.add(regularSearchResultItem);
        } while (nextButton != null);

        return results;
    }

    private WebElement getNextButton() {
        WebElement nextButton = null;
        try {
            nextButton = proxyWebEngine.webDriver.findElement(By.id("pnnext"));
        } catch(NoSuchElementException ex) {
            Logger.error(ex);
        }

        return nextButton;
    }
}
