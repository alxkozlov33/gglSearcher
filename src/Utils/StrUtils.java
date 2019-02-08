package Utils;

import Models.InputCsvModelItem;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrSubstitutor;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class StrUtils {
    public static String createURL(InputCsvModelItem csvItem, String inputPlaceHolder) {
        String result = null;
        if (!StringUtils.isEmpty(inputPlaceHolder)) {
            Map valuesMap = new HashMap();
            valuesMap.put("columnA", csvItem.getColumnA());
            valuesMap.put("columnB", csvItem.getColumnB());
            valuesMap.put("columnC", csvItem.getColumnC());
            valuesMap.put("columnD", csvItem.getColumnD());
            valuesMap.put("columnE", csvItem.getColumnE());
            valuesMap.put("columnF", csvItem.getColumnF());
            valuesMap.put("columnG", csvItem.getColumnG());
            valuesMap.put("columnH", csvItem.getColumnH());
            valuesMap.put("columnI", csvItem.getColumnI());
            valuesMap.put("columnJ", csvItem.getColumnJ());
            valuesMap.put("columnK", csvItem.getColumnK());
            valuesMap.put("columnL", csvItem.getColumnL());
            valuesMap.put("columnM", csvItem.getColumnM());
            valuesMap.put("columnN", csvItem.getColumnN());
            valuesMap.put("columnO", csvItem.getColumnO());
            StrSubstitutor sub = new StrSubstitutor(valuesMap);

            try {
                result = "https://duckduckgo.com/html/?q=" + URLEncoder.encode(sub.replace(inputPlaceHolder), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
