package Abstract.Strategies.Concrete;

import Abstract.Models.OutputModels.OutputRegularCSVItem;
import Abstract.Models.SearchResultModels.GoogleSearchResultItem;
import Abstract.Strategies.ISearchResultsConvertStrategy;

import java.util.List;

public class ConvertSearchResultsStrategy implements ISearchResultsConvertStrategy<GoogleSearchResultItem, OutputRegularCSVItem> {
    @Override
    public List<OutputRegularCSVItem> convertResultData(List<GoogleSearchResultItem> searchItems) {
        return null;
    }
}
