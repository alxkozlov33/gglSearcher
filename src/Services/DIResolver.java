package Services;

import Abstract.Strategies.SearchModeStrategyBase;
import org.tinylog.Logger;

public class DIResolver {

    private final UserAgentsRotatorService userAgentsRotatorService;
    private final GuiService guiService;
    private final OutputDataService outputDataService;
    private final InputDataService inputDataService;
    private final SettingsService settingsService;
    private final DBConnectionService dbConnectionService;
    private SearchModeStrategyBase currentWorker;

    public DIResolver(UserAgentsRotatorService userAgentsRotatorService,
                      GuiService guiService,
                      OutputDataService outputDataService,
                      InputDataService inputDataService,
                      SettingsService settingsService,
                      DBConnectionService dbConnectionService) {

        this.userAgentsRotatorService = userAgentsRotatorService;
        this.guiService = guiService;
        this.outputDataService = outputDataService;
        this.inputDataService = inputDataService;
        this.settingsService = settingsService;
        this.dbConnectionService = dbConnectionService;

        Logger.tag("SYSTEM").info("Application started...");
    }

    public void setCurrentWorker(SearchModeStrategyBase currentWorker) { this.currentWorker = currentWorker; }

    public SearchModeStrategyBase getCurrentWorker() { return this.currentWorker; }

    public UserAgentsRotatorService getUserAgentsRotatorService() {
        return userAgentsRotatorService;
    }

    public GuiService getGuiService() {
        return guiService;
    }

    public OutputDataService getOutputDataService() { return outputDataService; }

    public InputDataService getInputDataService() {
        return inputDataService;
    }

    public SettingsService getSettingsService() {
        return settingsService;
    }

    public DBConnectionService getDbConnectionService() {
        return dbConnectionService;
    }
}
