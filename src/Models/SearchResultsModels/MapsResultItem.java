package Models.SearchResultsModels;

import Abstract.GoogleSearchResultItem;
import org.jsoup.nodes.Element;

import java.util.List;

public class MapsResultItem extends GoogleSearchResultItem {
    @Override
    public List<GoogleSearchResultItem> getResults(Element html) {
        return null;
    }
}
