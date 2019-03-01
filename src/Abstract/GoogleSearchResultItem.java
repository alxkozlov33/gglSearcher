package Abstract;

import org.jsoup.nodes.Element;

import java.util.List;

public abstract class GoogleSearchResultItem {
    protected String mainHeader;
    protected String link;
    protected String description;

    public abstract List<GoogleSearchResultItem> getResults(Element html);
}
