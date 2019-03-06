package Services;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class LogService {

    //private final GuiService guiService;
    public LogService() {
        //this.guiService = DIResolver.getGuiService();
    }

    public void LogMessage(String message) {
        String msg = GetTimeString() + message;
        //guiService.logAction(msg);
    }

    private String GetTimeString() {
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();
        return "["+date.format(DateTimeFormatter.ISO_LOCAL_DATE) + " " +time.format(DateTimeFormatter.ISO_LOCAL_TIME)+"]: ";
    }

    public void drawLine() {
        LogMessage("_________________________________________________________________");
    }


}
