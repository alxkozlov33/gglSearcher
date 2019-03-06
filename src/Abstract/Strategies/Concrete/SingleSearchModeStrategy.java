package Abstract.Strategies.Concrete;

import Abstract.OutputModels.OutputCsvModelItem;
import Abstract.Strategies.ISearchModeStrategy;
import Models.SearchResultsModels.SearchResult;
import Services.FileService;
import Services.GuiService;
import Services.PropertiesService;
import org.jsoup.nodes.Element;

import java.util.ArrayList;

public class SingleSearchModeStrategy implements ISearchModeStrategy {

    @Override
    public void processData(FileService fileService, PropertiesService propertiesService, GuiService guiService) {
        Element body = getQueryBody(null);
        if (body != null) {
            SearchResult result = new SearchResult()
                    .initSearchExceptions(searchExceptions)
                    .parsePageBody(body);
            ArrayList<OutputCsvModelItem> items = fileService.mapSearchResultsToOutputCSVModels(result);
            fileService.SaveResultCsvItems(items);
        }
    }
}
