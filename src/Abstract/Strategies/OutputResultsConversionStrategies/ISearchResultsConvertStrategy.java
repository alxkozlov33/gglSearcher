package Abstract.Strategies.OutputResultsConversionStrategies;

import Abstract.Models.OutputModels.IOutputModel;
import Abstract.Models.SearchResultModels.GoogleSearchResultItem;

import java.util.List;

public interface ISearchResultsConvertStrategy<T extends GoogleSearchResultItem, U extends IOutputModel> {

    List<U> convertResultData(List<T> searchItems);
}
