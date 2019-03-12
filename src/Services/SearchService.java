package Services;

import Abstract.Engines.WebUrlEngine;
import Abstract.Factories.Concrete.SearchingModeFactory;
import Abstract.Models.SearchResultModels.GoogleSearchResultItem;
import Abstract.Models.SearchResultModels.WebPageObject;
import Abstract.Specifications.AbstractSpecification;
import Abstract.Specifications.Concrete.DomainExceptionsSpecification;
import Abstract.Specifications.Concrete.TopLevelDomainExceptionsSpecification;
import Abstract.Specifications.Concrete.URLExceptionsSpecification;
import Abstract.Models.RequestData;
import Abstract.Strategies.ISearchModeStrategy;
import Utils.ResultsUtils;
import org.apache.commons.lang.StringUtils;
import org.jsoup.nodes.Element;
import org.tinylog.Logger;

import java.util.ArrayList;
import java.util.List;

public class SearchService {

    public WebPageObject getWebSitePageSource(GoogleSearchResultItem item) {
        RequestData requestData = new RequestData(item.getLink());
        Element element = new WebUrlEngine().getWebSourceData(requestData);
        return parseSourceData(element);
    }

    public void StartWork() {
        GuiService guiService = new GuiService();
        String placeHolderText = guiService.getSearchPlaceholderText();
        try {
            if (StringUtils.isEmpty(placeHolderText)) {
                Logger.tag("SYSTEM").error("Placeholder text empty");
                return;
            }

            SettingsService settingsService = new SettingsService();
            settingsService.initSettingsFileData();

            SearchingModeFactory searchingModeFactory = new SearchingModeFactory();
            ISearchModeStrategy searchModeStrategy =  searchingModeFactory.createSearchModeStrategy(placeHolderText);

            searchModeStrategy.processData();

        } catch (Exception e) {
            guiService.changeApplicationStateToWork(false);
            Logger.tag("SYSTEM").error(e, "Application stopped");
        }
    }

    private WebPageObject parseSourceData(Element html){
        if (html == null) {
            return null;
        }
        String siteDescription = html.select("meta[name=description]").attr("content");
        String siteKeywords = html.select("meta[name=keywords]").attr("content");
        String siteName = html.select("meta[property=og:title]").attr("content");
        if (StringUtils.isEmpty(siteName)) {
            siteName = html.select("title").text();
        }
        return new WebPageObject(siteDescription, siteKeywords, siteName);
    }

    public <T extends GoogleSearchResultItem> ArrayList filterGoogleResultData(List<T> googleSearchResults) {
        SettingsService settingsService = new SettingsService();

        AbstractSpecification<GoogleSearchResultItem> googleItemsSpec =
                new DomainExceptionsSpecification(settingsService.getSearchSettings().domainExceptions)
                        .and(new TopLevelDomainExceptionsSpecification(settingsService.getSearchSettings().topLevelDomainsExceptions))
                        .and(new URLExceptionsSpecification(settingsService.getSearchSettings().URLExceptions));

        return ResultsUtils.filterResults(googleSearchResults, googleItemsSpec);
    }
}
