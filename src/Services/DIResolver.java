package Services;

import GUI.Bootstrapper;
import org.pmw.tinylog.Logger;

public class DIResolver {

    private FileService fileService;
    private LogService logService;
    private GuiService guiService;
    private SearchingProcessor searchingProcessor;

    public DIResolver() {
    }

    public void initDependencies () {
        getGuiService();
        getLogService();
        getFileService();
        getSearchingProcessor();

        Logger.info("Application started...");
    }
    public GuiService getGuiService() {
        if (guiService == null) {
            guiService = new GuiService();
        }
        return guiService;
    }

    public SearchingProcessor getSearchingProcessor() {
        if (searchingProcessor == null) {
            searchingProcessor = new SearchingProcessor(fileService, guiService);
        }
        return searchingProcessor;
    }

    public FileService getFileService() {
        if (fileService == null) {
            fileService = new FileService();
        }
        return fileService;
    }

    public LogService getLogService() {
        if (logService == null) {
            logService = new LogService();
        }
        return logService;
    }
}
