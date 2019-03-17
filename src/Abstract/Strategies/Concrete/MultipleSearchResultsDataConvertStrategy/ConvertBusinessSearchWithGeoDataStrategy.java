package Abstract.Strategies.Concrete.MultipleSearchResultsDataConvertStrategy;

import Abstract.Models.OutputModels.IOutputModel;
import Abstract.Models.OutputModels.OutputModelGeoDataDecorator;
import Abstract.Models.OutputModels.OutputRegularCSVItem;
import Abstract.Models.SearchResultModels.BusinessListSearchResultItem;
import Abstract.Strategies.ISearchResultsConvertStrategy;
import java.util.ArrayList;
import java.util.List;

public class ConvertBusinessSearchWithGeoDataStrategy implements ISearchResultsConvertStrategy<BusinessListSearchResultItem, IOutputModel> {

    private String city;
    private String country;

    public ConvertBusinessSearchWithGeoDataStrategy(String city, String country) {
        this.city = city;
        this.country = country;
    }

    @Override
    public List<IOutputModel> convertResultData(List<BusinessListSearchResultItem> searchItems) {
        ArrayList<IOutputModel> outputItems = new ArrayList<>();
        if (searchItems.size() == 0) {
            return null;
        }

        for (BusinessListSearchResultItem businessListSearchResultItem : searchItems) {
                String galleryName = businessListSearchResultItem.getMainHeader();
                String notSureLink = "";
                String webSite = "";
                String htmlPageTitle = "";
                this.city = businessListSearchResultItem.getCity();
                this.country = businessListSearchResultItem.getCountry();
                OutputRegularCSVItem outputRegularCSVItem = new OutputRegularCSVItem(galleryName, webSite, notSureLink, htmlPageTitle);
                OutputModelGeoDataDecorator outputModelGeoDataDecorator = new OutputModelGeoDataDecorator(outputRegularCSVItem, city, country);
                outputItems.add(outputModelGeoDataDecorator);
            }
        return outputItems;
    }
}
