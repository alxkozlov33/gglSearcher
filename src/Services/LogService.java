package Services;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class LogService {

    private GuiService guiService;
    public LogService(GuiService guiService) {
        this.guiService = guiService;
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
}
