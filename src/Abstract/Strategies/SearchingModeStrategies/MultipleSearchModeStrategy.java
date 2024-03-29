package Abstract.Strategies.SearchingModeStrategies;

import Abstract.Exceptions.InputFileEmptyException;
import Abstract.Models.SearchResultModels.GoogleSearchResultItem;
import Abstract.Specifications.AbstractSpecification;
import Abstract.Strategies.SearchModeStrategyBase;
import Abstract.Models.InputModels.InputCsvModelItem;
import Abstract.Models.RequestData;
import Abstract.Tasks.Worker;
import Services.*;
import Utils.StrUtils;
import org.tinylog.Logger;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class MultipleSearchModeStrategy extends SearchModeStrategyBase {

    private boolean isWorkFlag;
    private ThreadPoolExecutor executor;
    private final DIResolver diResolver;

    public MultipleSearchModeStrategy(DIResolver diResolver) {
        this.diResolver = diResolver;
    }

    @Override
    public void processData() throws InputFileEmptyException {
        AbstractSpecification<GoogleSearchResultItem> googleItemsSpec = getSettingsSpecification(diResolver);

        isWorkFlag  = true;
        diResolver.getGuiService().setStatusText("Processing started");
        List<InputCsvModelItem> csvFileData = diResolver.getInputDataService().getInputCsvModelItems();
        int size = diResolver.getInputDataService().getInputCsvModelItems().size();

        if (size == 0) {
            throw new InputFileEmptyException("Input data file doesn't contain elements");
        }
        executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(50);

        for (int i = 0; i < size; i++) {
            String URL = StrUtils.createUrlForMultipleSearch(csvFileData.get(i), diResolver.getGuiService().getSearchPlaceholderText());
            String requestTerm = StrUtils.createSearchTermForMultipleSearch(csvFileData.get(i), diResolver.getGuiService().getSearchPlaceholderText());
            RequestData requestData = new RequestData(URL, 10, getRandomNumberInRange(2000, 4000), csvFileData.get(i));
            requestData.setRequestTerm(requestTerm);
            Runnable worker = new Worker(diResolver, requestData, googleItemsSpec);
            executor.execute(worker);
        }

        while (!executor.isTerminated()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Logger.tag("SYSTEM").error(e);
            }
            if (isWorkFlag) {
                diResolver.getGuiService().updateCountItemsStatus((int)executor.getCompletedTaskCount(), (int)executor.getTaskCount());
            }
            if (executor.getCompletedTaskCount() == size) {
                stopProcessing();
            }
        }
    }

    public void stopProcessing() {
        executor.shutdown();
        isWorkFlag = false;
        diResolver.getDbConnectionService().updateWorkStatus(isWorkFlag);
    }

    private static int getRandomNumberInRange(int min, int max) {

        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }
}
