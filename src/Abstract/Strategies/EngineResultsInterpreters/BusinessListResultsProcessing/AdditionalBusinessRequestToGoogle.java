package Abstract.Strategies.EngineResultsInterpreters.BusinessListResultsProcessing;
import Abstract.Engines.ProxyWebClient;
import Abstract.Engines.ProxyWebEngine;
import Abstract.Models.RequestData;
import Abstract.Models.SearchResultModels.BusinessListSearchResultItem;
import Abstract.Specifications.AbstractSpecification;
import Abstract.Specifications.Concrete.WebsiteSpecifications.IsFacebookSpecification;
import Abstract.Specifications.Concrete.WebsiteSpecifications.IsInstagramSpecification;
import Abstract.Specifications.Concrete.WebsiteSpecifications.IsTwitterSpecification;
import Services.DIResolver;
import Utils.StrUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.tinylog.Logger;

class AdditionalBusinessRequestToGoogle {
    private final ProxyWebClient proxyWebClient;
    private final AbstractSpecification<String> socialMediaSpecification;

    AdditionalBusinessRequestToGoogle(DIResolver diResolver) {
        this.proxyWebClient = new ProxyWebClient(diResolver);
        this.socialMediaSpecification = new IsFacebookSpecification().or(new IsInstagramSpecification()).or(new IsTwitterSpecification());
    }

    synchronized void processData(BusinessListSearchResultItem businessListSearchResultItem) {
        ProxyWebEngine proxyWebEngine = new ProxyWebEngine();
        proxyWebEngine.webDriver.navigate().to(StrUtils.createUrlForAdditionalPlacesSearch(businessListSearchResultItem));
        Element element = Jsoup.parse(proxyWebEngine.webDriver.getPageSource());

        WebElement resultsAbout;
        try {
            resultsAbout = proxyWebEngine.webDriver.findElementByXPath("//*[@id=\"rhs_block\"]/div/div/div/div[1]/div[2]/div[2]/div/a");
            resultsAbout.click();
            element = Jsoup.parse(proxyWebEngine.webDriver.getPageSource());
        } catch (NoSuchElementException ex) {
            Logger.error(ex, "Cannot locate \"results about\" button");
        }

        String webSite = null;
        try {
            webSite = element.selectFirst("#rhs_block").selectFirst("a:contains(Website)").attr("href");
            webSite = StrUtils.extractWebSiteFromURL(webSite);
        } catch (Exception ex) {
            Logger.error("Cannot locate details");
        }

        if (webSite == null || socialMediaSpecification.isSatisfiedBy(webSite)) {
            return;
        }
        businessListSearchResultItem.setLink(webSite);
    }
}
