package Models.ProxyObjects;

import org.apache.commons.lang.StringUtils;

public class ProxyObjectDto {
    public String ip;
    public int port;

    public ProxyObjectDto(String proxyString) {
        if (!StringUtils.isEmpty(proxyString)) {
            ip = proxyString.split(":")[0];
            port = Integer.parseInt(proxyString.split(":")[1]);
        }
    }
}
