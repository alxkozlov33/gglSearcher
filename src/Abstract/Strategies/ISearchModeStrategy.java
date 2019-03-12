package Abstract.Strategies;

import Services.DIResolver;

public interface ISearchModeStrategy {
    void processData(DIResolver diResolver);
    void stopProcessing();
}
