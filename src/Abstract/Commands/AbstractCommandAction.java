package Abstract.Commands;

import Services.*;

import javax.swing.*;

public abstract class AbstractCommandAction extends AbstractAction {
    protected FileService fileService;
    protected GuiService guiService;
    protected LogService logService;
    protected PropertiesService propertiesService;
    protected SearchService searchService;
    protected UserAgentsRotatorService userAgentsRotatorService;

    public AbstractCommandAction(DIResolver diResolver, String name) {
        super(name);
        this.fileService = diResolver.getFileService();
        this.guiService = diResolver.getGuiService();
        this.logService = diResolver.getLogService();
        this.propertiesService = diResolver.getPropertiesService();
        this.searchService = diResolver.getSearchService();
        this.userAgentsRotatorService = diResolver.getUserAgentsRotatorService();
    }
}