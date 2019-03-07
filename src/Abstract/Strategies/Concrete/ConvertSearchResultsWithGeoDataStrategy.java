package Abstract.Strategies.Concrete;

import Abstract.Engines.WebUrlEngine;
import Abstract.OutputModels.OutputRegularCSVItem;
import Abstract.SearchResultModels.GoogleSearchResultItem;
import Abstract.Strategies.ISearchResultsConvertStrategy;
import Models.RequestData;
import Services.ProxyService;
import Services.UserAgentsRotatorService;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;

public class ConvertSearchResultsWithGeoDataStrategy implements ISearchResultsConvertStrategy<GoogleSearchResultItem, OutputRegularCSVItem> {

    private String city;
    private String country;

    public ConvertSearchResultsWithGeoDataStrategy(String city, String country) {
        this.city = city;
        this.country = country;
    }

    @Override
    public List<OutputRegularCSVItem> convertResultData(List<GoogleSearchResultItem> searchItems) {
        ArrayList<OutputRegularCSVItem> outputItems = new ArrayList<>();
        if (searchItems.size() == 0) {
            return null;
        }
        for (GoogleSearchResultItem item : searchItems) {
            checkWebSite(item);
            outputItems.add(new OutputRegularCSVItem(item.getGalleryName(), item.getWebsite(), item.getCity(), item.getNotSureLink(), item.getCountry()));
        }

//        if (str.length() > 15){
//            NotSureLink = link;
//        }
//        else {
//            Website = StrUtils.extractDomainName(link);
//        }
//
//        if (StringUtils.isEmpty(siteName)) {
//            GalleryName = mainHeader;
//        }
//        else {
//            GalleryName = siteName;
//        }

        return outputItems;
    }

    private checkWebSite(GoogleSearchResultItem item) {
        ProxyService proxyService = new ProxyService();
        UserAgentsRotatorService userAgentsRotatorService = new UserAgentsRotatorService();

        RequestData requestData = new RequestData(item.link, userAgentsRotatorService.getRandomUserAgent(), proxyService.getNewProxyAddress());

        Element element = new WebUrlEngine().getWebSourceData(requestData);

    }
}
