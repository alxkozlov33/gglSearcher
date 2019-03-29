package Abstract.Strategies.EngineResultsInterpreters.BusinessListResultsProcessing;

import Abstract.Engines.ProxyWebEngine;
import Abstract.Models.SearchResultModels.BusinessListSearchResultItem;
import Utils.StrUtils;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import java.util.ArrayList;
import java.util.List;

public abstract class BusinessResultItemsProcess {
    public abstract List<BusinessListSearchResultItem> processBody(ProxyWebEngine proxyWebEngine);

    //public abstract List<BusinessListSearchResultItem> processBody(Element body);

    //TODO: Remove this shit
    Elements findBussinessGeoDataByNames(Elements names) {
        Elements addressesElements = new Elements();
        for (Element element : names) {
            if (element.parent().childNodeSize() == 4 || element.parent().childNodeSize() == 5) {
                addressesElements.add(element.parent().child(2).select("span").first());
            } else if (element.parent().childNodeSize() == 2) {
                addressesElements.add(element.parent().child(1).child(1).select("span").first());
            } else if (element.parent().childNodeSize() == 3) {
                if (element.parent().child(2).childNodeSize() == 1) {
                    if (element.parent().child(2).child(0).childNodeSize() == 1) {
                        addressesElements.add(element.parent().child(2).child(0).select("span").first());
                    } else if (element.parent().child(2).child(1).childNodeSize() > 1) {
                        addressesElements.add(element.parent().child(2).child(1).select("span").first());
                    }
                } else if (element.parent().child(2).childNodeSize() == 2) {
                    if (element.parent().child(2).child(1).childNodeSize() == 0) {
                        addressesElements.add(new Element("<div>No data</div>"));
                    } else if (element.parent().child(2).child(1).childNodeSize() > 1) {
                        addressesElements.add(element.parent().child(2).child(1).select("span").first());
                    }
                } else if (element.parent().child(2).childNodeSize() == 3 || element.parent().child(2).childNodeSize() == 4) {
                    addressesElements.add(element.parent().child(2).child(1).select("span").first());
                }
            }
        }
        return addressesElements;
    }
    
    List processMapsPage(ProxyWebEngine proxyWebEngine) throws InterruptedException {
        ArrayList<BusinessListSearchResultItem> results = new ArrayList<>();
        WebDriver webDriver = proxyWebEngine.webDriver;

        WebElement nextButton = webDriver.findElement(By.id("pnnext"));
        while(nextButton != null) {
            List<WebElement> elements = webDriver.findElements((By.cssSelector("div[aria-level=\"3\"]")));
            for (WebElement item : elements) {
                String galleryName = item.getText();
                item.click();

                //TODO: There are code not work.
                String webSite = item.findElement(By.cssSelector("a:contains('Website')")).getAttribute("href");
                String address = item.findElement(By.cssSelector("div[data-md=\"1002\"]")).getText();
                BusinessListSearchResultItem regularSearchResultItem = new BusinessListSearchResultItem(galleryName, webSite , "", address, StrUtils.getCountryFromAddress(address));
                results.add(regularSearchResultItem);
            }
            nextButton = webDriver.findElement(By.id("pnnext"));
            nextButton.click();
            Thread.sleep(1000);
        }
        return results;
    }
}
