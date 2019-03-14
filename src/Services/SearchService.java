package Services;

import Abstract.Factories.Concrete.SearchingModeFactory;
import Abstract.Strategies.SearchModeStrategyBase;
import org.apache.commons.lang.StringUtils;
import org.tinylog.Logger;

public class SearchService {

    public void StartWork(DIResolver diResolver) {
        GuiService guiService = new GuiService();
        String placeHolderText = guiService.getSearchPlaceholderText();
        try {
            if (StringUtils.isEmpty(placeHolderText)) {
                Logger.tag("SYSTEM").error("Placeholder text empty");
                return;
            }

            SettingsService settingsService = new SettingsService();
            settingsService.initSettingsFileData();

            SearchingModeFactory searchingModeFactory = new SearchingModeFactory();
            SearchModeStrategyBase searchModeStrategy =  searchingModeFactory.createSearchModeStrategy(placeHolderText);

            searchModeStrategy.processData(diResolver);

        } catch (Exception e) {
            guiService.changeApplicationStateToWork(false);
            Logger.tag("SYSTEM").error(e, "Application stopped");
        }
    }
}
