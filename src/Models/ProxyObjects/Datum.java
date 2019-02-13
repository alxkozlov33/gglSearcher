package Models.ProxyObjects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

    @SerializedName("ipPort")
    @Expose
    public String ipPort;
    @SerializedName("ip")
    @Expose
    public String ip;
    @SerializedName("port")
    @Expose
    public String port;
    @SerializedName("country")
    @Expose
    public String country;
    @SerializedName("last_checked")
    @Expose
    public String lastChecked;
    @SerializedName("proxy_level")
    @Expose
    public String proxyLevel;
    @SerializedName("type")
    @Expose
    public String type;
    @SerializedName("speed")
    @Expose
    public String speed;
    @SerializedName("support")
    @Expose
    public Support support;

}