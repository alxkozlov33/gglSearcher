package Services;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class LogService {

    private final GuiService guiService;
    public LogService() {
        this.guiService = DIResolver.getGuiService();
    }

    public void LogMessage(String message) {
        String msg = GetTimeString() + message;
        guiService.logAction(msg);
    }

    public void UpdateStatus(String message) {
        guiService.getBootstrapper().getLabelStatusData().setText(message);
    }

    private String GetTimeString() {
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();
        return "["+date.format(DateTimeFormatter.ISO_LOCAL_DATE) + " " +time.format(DateTimeFormatter.ISO_LOCAL_TIME)+"]: ";
    }

    public void drawLine() {
        LogMessage("_________________________________________________________________");
    }

    public void updateCountItemsStatus(int currentItem, int totalItems) {

        if (totalItems > 1) {
            guiService.getBootstrapper().getLabelStatusData().setText("Processed " + currentItem + "/" + (totalItems - 1) +" items.");
        }
        else {
            guiService.getBootstrapper().getLabelStatusData().setText("Processed " + currentItem + "/" + (totalItems) +" items");
        }
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
