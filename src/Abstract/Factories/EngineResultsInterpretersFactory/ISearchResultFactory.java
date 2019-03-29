package Abstract.Factories.EngineResultsInterpretersFactory;

import Abstract.Engines.ProxyWebEngine;
import org.jsoup.nodes.Element;

import java.util.List;

public interface ISearchResultFactory {

    List processBody(ProxyWebEngine proxyWebEngine);

    List processBody(Element body);
}
