package Services;

import Models.ProxyObjects.ProxyObjectDto;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.net.SocketTimeoutException;

public class ProxyService {
    private LogService logService;
    public ProxyService(LogService logService) {
        this.logService = logService;
    }
    public ProxyObjectDto getNewProxy() {
        Connection.Response response = null;
        String json = null;

        boolean success = false;

        while(!success) {
            try {
                response = Jsoup.connect("http://pubproxy.com/api/proxy?google=true&last_check=3&api=ZlBnbzgzUnhvUjBqbytFa1dZTzAzdz09&format=txt")
                        .ignoreContentType(true)
                        .userAgent("DuckDuckBot/1.0; (+http://duckduckgo.com/duckduckbot.html)")
                        .method(Connection.Method.GET)
                        .ignoreHttpErrors(true)
                        .timeout(10000)
                        .execute();

                json = response.parse().text();
                if (json != null) {
                    success = true;
                    break;
                }
            } catch (SocketTimeoutException ex) {
                logService.LogMessage("Cannot get proxy");
            } catch (IOException e) {
                logService.LogMessage("Cannot get proxy");
            }
            try {
                logService.LogMessage("Waiting 5 minutes to get proxy again...");
                Thread.sleep(300000);
            } catch (InterruptedException e) {
                logService.LogMessage("Thread sleep 300000 problem");
            }
            success = false;
        }

        if(success) {
            ProxyObjectDto dto = new ProxyObjectDto(json);
            return dto;
        }
        return null;
    }
}
