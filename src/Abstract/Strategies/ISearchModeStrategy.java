package Abstract.Strategies;

import Services.GuiService;

public interface ISearchModeStrategy {
    void processData(GuiService guiService);
    void stopProcessing();
}
