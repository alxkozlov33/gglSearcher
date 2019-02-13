package Models.ProxyObjects;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProxyObject {

    @SerializedName("data")
    @Expose
    public List<Datum> data = null;
    @SerializedName("count")
    @Expose
    public Integer count;
}