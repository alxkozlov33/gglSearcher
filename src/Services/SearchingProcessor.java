package Services;

import Abstract.Factories.Concrete.SearchingModeFactory;
import Abstract.Strategies.ISearchModeStrategy;
import org.apache.commons.lang.StringUtils;
import org.tinylog.Logger;


public class SearchingProcessor {

    private boolean isWorkFlag;
    public void StartWork(GuiService guiService) {
        String placeHolderText = guiService.getSearchPlaceholderText();
        try {
            if (StringUtils.isEmpty(placeHolderText)) {
                Logger.tag("SYSTEM").error("Placeholder text empty");
                return;
            }

            SearchingModeFactory searchingModeFactory = new SearchingModeFactory();
            ISearchModeStrategy searchModeStrategy =  searchingModeFactory.createSearchModeStrategy(placeHolderText);

            searchModeStrategy.processData(guiService);

        } catch (Exception e) {
            guiService.changeApplicationStateToWork(false);
            Logger.tag("SYSTEM").error(e, "Application stopped");
        }
    }

    public void setWorkStateToStop() {
        isWorkFlag = false;
    }
    public void setWorkFlagToRun() {
        isWorkFlag = true;
    }

}
