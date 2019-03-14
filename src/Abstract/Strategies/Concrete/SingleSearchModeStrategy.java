package Abstract.Strategies.Concrete;

import Abstract.Strategies.SearchModeStrategyBase;
import Services.DIResolver;

public class SingleSearchModeStrategy extends SearchModeStrategyBase {

    public void processData(DIResolver diResolver) {
        System.out.println("test");
//        Element body = getQueryBody(null);
//        if (body != null) {
//            SearchResult result = new SearchResult()
//                    .initSearchExceptions(searchExceptions)
//                    .parsePageBody(body);
//            ArrayList<OutputCsvModelItem> items = fileService.mapSearchResultsToOutputCSVModels(result);
//            fileService.SaveResultCsvItems(items);
//        }
    }

    public void stopProcessing() {

    }
}
