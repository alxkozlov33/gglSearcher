package Services;


import org.tinylog.Logger;

public class DIResolver {

    private final UserAgentsRotatorService userAgentsRotatorService;
    private final PropertiesService propertiesService;
    private final GuiService guiService;
    private final OutputDataService outputDataService;
    private final InputDataService inputDataService;
    private final SearchService searchService;
    private final SettingsService settingsService;

    public DIResolver(UserAgentsRotatorService userAgentsRotatorService,
                      PropertiesService propertiesService,
                      GuiService guiService,
                      OutputDataService outputDataService,
                      InputDataService inputDataService,
                      SearchService searchService,
                      SettingsService settingsService) {

        this.userAgentsRotatorService = userAgentsRotatorService;
        this.propertiesService = propertiesService;
        this.guiService = guiService;
        this.outputDataService = outputDataService;
        this.inputDataService = inputDataService;
        this.searchService = searchService;
        this.settingsService = settingsService;

        Logger.info("Application started...");
    }

    public UserAgentsRotatorService getUserAgentsRotatorService() {
        return userAgentsRotatorService;
    }

    public PropertiesService getPropertiesService() {
        return propertiesService;
    }

    public GuiService getGuiService() {
        return guiService;
    }

    public OutputDataService getOutputDataService() {
        return outputDataService;
    }

    public InputDataService getInputDataService() {
        return inputDataService;
    }

    public SearchService getSearchService() {
        return searchService;
    }

    public SettingsService getSettingsService() {
        return settingsService;
    }
}
