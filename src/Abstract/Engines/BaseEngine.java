package Abstract.Engines;

import org.apache.http.impl.client.CloseableHttpClient;

import java.util.Random;

abstract class BaseEngine {
    static final String username = "lum-customer-ihouse_d-zone-static";
    static final String password = "qs38hp672p1l";
    static final String hostName = "zproxy.lum-superproxy.io";
    final int port = 22225;
    CloseableHttpClient client;

    String getLoginName() {
        return username+"-session-" + Integer.toString(new Random().nextInt(Integer.MAX_VALUE));
    }
}
