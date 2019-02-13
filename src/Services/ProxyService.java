package Services;

import Models.ProxyObjects.ProxyObject;
import Models.ProxyObjects.ProxyObjectDto;
import com.google.gson.Gson;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;

public class ProxyService {

    public ProxyObjectDto getNewProxy() {
        Connection.Response response = null;
        String json = null;
        try {
            response = Jsoup.connect("http://pubproxy.com/api/proxy?google=true?api=ZlBnbzgzUnhvUjBqbytFa1dZTzAzdz09")
                    .ignoreContentType(true)
                    .userAgent("DuckDuckBot/1.0; (+http://duckduckgo.com/duckduckbot.html)")
                    .method(Connection.Method.GET)
                    .execute();
            json = response.parse().text();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Gson gson = new Gson(); // Or use new GsonBuilder().create();
        ProxyObject po = gson.fromJson(json, ProxyObject.class); // deserializes json into target2
        if (po.count == 0) {
            return null;
        }
        ProxyObjectDto dto = new ProxyObjectDto();
        dto.ip = po.data.get(0).ip;
        dto.port = Integer.parseInt(po.data.get(0).port);
        return dto;
    }
}
