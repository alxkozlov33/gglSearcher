package Abstract.ResultsFactories;

import Abstract.SearchResultModels.RegularSearchResult;
import org.jsoup.nodes.Element;

import java.util.List;

public class RegularResultsFactory implements IResultSearchFactory {
    @Override
    public List<RegularSearchResult> processBody(Element body) {
        return null;
    }
}
