package Abstract.Strategies.OutputResultsConversionStrategies.SingleSearchResultsDataConvertStrategy;

import Abstract.Models.OutputModels.IOutputModel;
import Abstract.Models.OutputModels.OutputRegularCSVItem;
import Abstract.Models.SearchResultModels.BusinessListSearchResultItem;
import Abstract.Strategies.OutputResultsConversionStrategies.SearchResultsConvertStrategy;

import java.util.ArrayList;
import java.util.List;

public class ConvertBusinessSearchDataStrategy extends SearchResultsConvertStrategy<BusinessListSearchResultItem, IOutputModel> {

    public ConvertBusinessSearchDataStrategy() {
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
                OutputRegularCSVItem outputRegularCSVItem = new OutputRegularCSVItem(galleryName, webSite, notSureLink, htmlPageTitle);
                outputItems.add(outputRegularCSVItem);
            }
        return outputItems;
    }
}
