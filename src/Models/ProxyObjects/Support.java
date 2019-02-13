package Models.ProxyObjects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Support {

    @SerializedName("https")
    @Expose
    public Integer https;
    @SerializedName("get")
    @Expose
    public Integer get;
    @SerializedName("post")
    @Expose
    public Integer post;
    @SerializedName("cookies")
    @Expose
    public Integer cookies;
    @SerializedName("referer")
    @Expose
    public Integer referer;
    @SerializedName("user_agent")
    @Expose
    public Integer userAgent;
    @SerializedName("google")
    @Expose
    public Integer google;

}