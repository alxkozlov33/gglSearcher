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

        int i = 0;
        boolean success = false;

        while(i < 3) {
            try {
                response = Jsoup.connect("http://pubproxy.com/api/proxy?google=true&last_check=3&api=ZlBnbzgzUnhvUjBqbytFa1dZTzAzdz09&format=txt")
                        .ignoreContentType(true)
                        .userAgent("DuckDuckBot/1.0; (+http://duckduckgo.com/duckduckbot.html)")
                        .method(Connection.Method.GET)
                        .ignoreHttpErrors(true)
                        .timeout(10000)
                        .execute();
                success = true;
                break;
            } catch (SocketTimeoutException ex) {
                logService.LogMessage("Cannot get proxy");
            } catch (IOException e) {
                logService.LogMessage("Cannot get proxy");
            }
            i++;
        }

        if(success) {
            try {
                json = response.parse().text();
            } catch (IOException e) {
                logService.LogMessage("Cannot parse body");
            }
//            Gson gson = new Gson();
//            ProxyObject po = gson.fromJson(json, ProxyObject.class);
//            if (po.count == 0) {
//                return null;
//            }
            ProxyObjectDto dto = new ProxyObjectDto(json);
//            dto.ip = po.data.get(0).ip;
//            dto.port = Integer.parseInt(po.data.get(0).port);
            return dto;
        }
        return null;
    }
}
