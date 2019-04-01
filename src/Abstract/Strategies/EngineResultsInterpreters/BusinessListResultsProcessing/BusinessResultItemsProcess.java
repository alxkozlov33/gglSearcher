package Abstract.Strategies.EngineResultsInterpreters.BusinessListResultsProcessing;

import Abstract.Engines.ProxyWebEngine;
import Abstract.Models.SearchResultModels.BusinessListSearchResultItem;
import Utils.StrUtils;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlLink;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.tinylog.Logger;
import java.io.IOException;
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

    List<BusinessListSearchResultItem> requestToGoogleMaps(ProxyWebEngine proxyWebEngine) {
        List results = new ArrayList<>();
        try {
            String queryTerm = proxyWebEngine.webDriver.findElementByName("q").getAttribute("value");
            String requestURL = StrUtils.createUrlForMapsSearching(queryTerm);
            HtmlPage page = proxyWebEngine.webClient.getPage(requestURL);
            proxyWebEngine.webClient.waitForBackgroundJavaScript(30000);
            HtmlDivision first = page.getFirstByXPath("//*[@id=\"pane\"]/div/div[1]/div/div/div[2]/div[1]");
            String firstText = first.asText();
            first.click();

            proxyWebEngine.webClient.waitForBackgroundJavaScript(30000);
            HtmlDivision details  = page.getFirstByXPath("//*[@id=\"pane\"]/div/div[1]/div/div");
            String detailsText = details.asText();
            //link.getWebResponse(true);
            //List images = page.getByXPath("//img");
            //image.getImageReader();
            //String test = Jsoup.connect(requestURL).execute().body();
            //results = processMapsPage(proxyWebEngine);
        } catch (IOException e) {
            Logger.error(e);
        }
        return results;
    }

    public static ExpectedCondition<Boolean> waitForLoad() {
        return new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                return ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete");
            }
        };
    }

    //https://www.google.pl/maps/search/art+gallery+Woodford+Green+England
//    List processMapsPage(ProxyWebEngine proxyWebEngine) throws InterruptedException {
//        ArrayList<BusinessListSearchResultItem> results = new ArrayList<>();
//
//        WebElement nextButton = webDriver.findElement(By.id("pnnext"));
//        while(nextButton != null) {
//            List<WebElement> elements = webDriver.findElements((By.cssSelector("a.rllt__link")));
//            String url = webDriver.getCurrentUrl();
//            for (WebElement item : elements) {
//                String galleryName = item.getText();
//                item.click();
//
//                proxyWebEngine.webClient.getPage()
//                try (final WebClient webClient = new WebClient()) {
//
//                    new WebConnectionWrapper(webClient) {
//                        @Override
//                        public WebResponse getResponse(WebRequest request) throws IOException {
//                            WebResponse response = super.getResponse(request);
//                            System.out.println(request.getUrl());
//                            System.out.println(response.getStatusCode());
//                            return response;
//                        }
//                    };
//
//                    String url = "http://localhost/test.html";
//                    HtmlPage page = webClient.getPage(url);
//
//                    // to wait for AJAX
//                    webClient.waitForBackgroundJavaScript(3000);
//
//                    // to forcibly load the link
//                    HtmlLink link = page.getFirstByXPath("//link");
//                    link.getWebResponse(true);
//
//                    // to forcibly load the image
//                    HtmlImage image = page.getFirstByXPath("//img");
//                    image.getImageReader();
//                }
//
//                webDriver.navigate().refresh();
//                By detailsContainer = By.cssSelector("div.immersive-container");
//                Wait<WebDriver> wait = new FluentWait<>(webDriver)
//                        .withTimeout(60, TimeUnit.SECONDS)
//                        .pollingEvery(5, TimeUnit.SECONDS)
//                        .ignoring(NoSuchElementException.class);
//                wait.until(ExpectedConditions
//                        .visibilityOfElementLocated(detailsContainer));
//                //webDriver.findElement(By.xpath("//*[@id=\"rhs_block\"]/div/div[2]"))
//                //TODO: There are code not work.rllt__link
//                String webSite = webDriver.findElement(By.cssSelector("a:contains('Website')")).getAttribute("href");
//                String address = webDriver.findElement(By.cssSelector("div[data-md=\"1002\"]")).getText();
//                BusinessListSearchResultItem regularSearchResultItem = new BusinessListSearchResultItem(galleryName, webSite , "", address, StrUtils.getCountryFromAddress(address));
//                results.add(regularSearchResultItem);
//            }
//            nextButton = webDriver.findElement(By.id("pnnext"));
//            nextButton.click();
//            Thread.sleep(1000);
//        }
//        return results;
//    }
}
