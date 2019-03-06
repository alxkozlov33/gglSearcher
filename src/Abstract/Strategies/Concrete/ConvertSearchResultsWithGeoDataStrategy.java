package Abstract.Strategies.Concrete;

import Abstract.OutputModels.OutputCsvModelItem;
import Abstract.OutputModels.OutputRegularCSVItem;
import Abstract.SearchResultModels.GoogleSearchResultItem;
import Abstract.Strategies.ISearchResultsConvertStrategy;

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
            outputItems.add(new OutputRegularCSVItem(item.getGalleryName(), item.getWebsite(), item.getCity(), item.getNotSureLink(), item.getCountry()));
        }
        return outputItems;
    }
}
