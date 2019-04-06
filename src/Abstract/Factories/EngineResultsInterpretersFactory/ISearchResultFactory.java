package Abstract.Factories.EngineResultsInterpretersFactory;

import org.jsoup.nodes.Element;

import java.util.List;

public interface ISearchResultFactory {
    List getRegularSearchStrategy(Element body);
}
