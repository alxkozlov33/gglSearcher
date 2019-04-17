package Abstract.Strategies.EngineResultsInterpreters.BusinessListResultsProcessing;
import Abstract.Models.SearchResultModels.BusinessListSearchResultItem;
import Abstract.Specifications.AbstractSpecification;
import Abstract.Specifications.Concrete.WebsiteSpecifications.IsFacebookSpecification;
import Abstract.Specifications.Concrete.WebsiteSpecifications.IsInstagramSpecification;
import Abstract.Specifications.Concrete.WebsiteSpecifications.IsTwitterSpecification;

class AdditionalBusinessRequestToGoogle {
    private final AbstractSpecification<String> socialMediaSpecification;

    AdditionalBusinessRequestToGoogle() {
        this.socialMediaSpecification = new IsFacebookSpecification().or(new IsInstagramSpecification()).or(new IsTwitterSpecification());
    }

    synchronized void processData(BusinessListSearchResultItem businessListSearchResultItem) {
//        ProxyWebEngine proxyWebEngine = new ProxyWebEngine();
//        proxyWebEngine.webDriver.navigate().to(StrUtils.createUrlForAdditionalPlacesSearch(businessListSearchResultItem));
//        Element element = Jsoup.parse(proxyWebEngine.webDriver.getPageSource());
//
//        WebElement resultsAbout;
//        try {
//            resultsAbout = proxyWebEngine.webDriver.findElementByXPath("//*[@id=\"rhs_block\"]/div/div/div/div[1]/div[2]/div[2]/div/a");
//            resultsAbout.click();
//            element = Jsoup.parse(proxyWebEngine.webDriver.getPageSource());
//        } catch (Exception ex) {
//            Logger.info(ex, "Cannot locate \"results about\" button");
//        }
//
//        String webSite = null;
//        try {
//            webSite = element.selectFirst("#rhs_block").selectFirst("a:contains(Website)").attr("href");
//            webSite = StrUtils.extractWebSiteFromURL(webSite);
//        } catch (Exception ex) {
//            Logger.error("Cannot locate details");
//        }
//
//        if (webSite == null || socialMediaSpecification.isSatisfiedBy(webSite)) {
//            return;
//        }
//        businessListSearchResultItem.setLink(webSite);
    }
}
