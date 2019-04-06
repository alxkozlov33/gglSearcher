package Abstract.Strategies.EngineResultsInterpreters.BusinessListResultsProcessing;

import Abstract.Engines.ProxyWebEngine;
import Abstract.Models.InputModels.InputCsvModelItem;
import Abstract.Models.SearchResultModels.BusinessListSearchResultItem;
import Services.DIResolver;
import org.openqa.selenium.*;
import org.tinylog.Logger;
import java.util.ArrayList;
import java.util.List;

public class BusinessResultItemsProcess {
    private ProxyWebEngine proxyWebEngine;
    private final DIResolver diResolver;

    public BusinessResultItemsProcess(DIResolver diResolver) {
        this.diResolver = diResolver;
    }

    public List<BusinessListSearchResultItem> processData(ProxyWebEngine proxyWebEngine, InputCsvModelItem inputCsvModelItem) {
        this.proxyWebEngine = proxyWebEngine;

        List<BusinessListSearchResultItem> results = new ArrayList<>();
        WebElement linkToMaps = proxyWebEngine.webDriver.findElementByCssSelector("#lu_map"); ////*[@id="rso"]/div[2]/div/div/div[2]/div/div[1]/a
        linkToMaps.click();

        WebElement nextButton;
        AdditionalBusinessRequestToGoogle additionalBusinessRequestToGoogle = new AdditionalBusinessRequestToGoogle(diResolver);
        do {
            nextButton = getNextButton();
            int placesSize = proxyWebEngine.webDriver.findElements((By.cssSelector("a.rllt__link"))).size();
            for (int i = 1; i <= placesSize; i++) {
                String placeName = proxyWebEngine.webDriver.findElement((By.xpath("//*[@id=\"rl_ist0\"]/div[1]/div[4]/div[" + i + "]/div/div[3]/div/a/div/div[2]/div"))).getText();
                BusinessListSearchResultItem regularSearchResultItem = new BusinessListSearchResultItem(placeName, "", "", inputCsvModelItem);
                additionalBusinessRequestToGoogle.processData(regularSearchResultItem);
                results.add(regularSearchResultItem);
            }

            if (nextButton != null) {
                nextButton.click();
            }
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
