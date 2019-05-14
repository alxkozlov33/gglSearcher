package Abstract.Models;

import Abstract.Models.InputModels.InputCsvModelItem;

public class RequestData {
    public String requestURL;
    private String requestTerm;
    public int attemptsCount;
    public int requestDelay;
    public InputCsvModelItem inputCsvModelItem;

    public RequestData(String requestURL, int attemptsCount, int requestDelay) {
        this.requestURL = requestURL;
        this.attemptsCount = attemptsCount;
        this.requestDelay = requestDelay;
    }

    public RequestData(String requestURL, int attemptsCount, int requestDelay, InputCsvModelItem inputCsvModelItem) {
        this.requestURL = requestURL;
        this.attemptsCount = attemptsCount;
        this.requestDelay = requestDelay;
        this.inputCsvModelItem = inputCsvModelItem;
    }

    public String getRequestTerm() {
        return requestTerm;
    }

    public void setRequestTerm(String requestTerm) {
        this.requestTerm = requestTerm;
    }
}