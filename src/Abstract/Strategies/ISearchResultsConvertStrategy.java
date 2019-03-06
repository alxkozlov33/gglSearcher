package Abstract.Strategies;

import Abstract.OutputModels.OutputCsvModelItem;
import Abstract.SearchResultModels.GoogleSearchResultItem;

import java.util.List;

public interface ISearchResultsConvertStrategy<T extends GoogleSearchResultItem, U extends OutputCsvModelItem> {

    List<U> convertResultData(List<T> searchItems);
}
