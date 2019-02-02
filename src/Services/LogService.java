package Services;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class LogService {

    GuiService guiService;
    public LogService(GuiService guiService) {
        this.guiService = guiService;
    }

    public void logMessage(String message) {
        String msg = getTimeString() + message;
        guiService.logAction(msg);
    }

    private String getTimeString() {
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();
        return "["+date.format(DateTimeFormatter.ISO_LOCAL_DATE) + " " +time.format(DateTimeFormatter.ISO_LOCAL_TIME)+"]: ";
    }
}
