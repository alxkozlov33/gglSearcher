package Abstract.Strategies.Concrete;

import Abstract.Strategies.ISearchModeStrategy;

public class SingleSearchModeStrategy implements ISearchModeStrategy {

    @Override
    public void processData() {
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

    @Override
    public void stopProcessing() {

    }
}
