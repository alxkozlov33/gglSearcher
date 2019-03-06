package Abstract.Strategies;

import Services.FileService;
import Services.GuiService;
import Services.PropertiesService;

public interface ISearchModeStrategy {
    void processData(FileService fileService, PropertiesService propertiesService, GuiService guiService);
}
