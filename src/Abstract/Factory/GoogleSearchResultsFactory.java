package Abstract.Factory;

import Abstract.GoogleSearchResults;
import org.jsoup.nodes.Element;

public abstract class GoogleSearchResultsFactory {

    public abstract GoogleSearchResults buildResults(Element body);
}
