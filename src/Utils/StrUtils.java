package Utils;

import Models.InputCsvModelItem;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrSubstitutor;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StrUtils {
    public static String createURL(InputCsvModelItem csvItem, String inputPlaceHolder) {
        String result = null;
        if (StringUtils.isEmpty(inputPlaceHolder)) {
            return "";
        }
        StrSubstitutor sub = new StrSubstitutor(valuesMap(csvItem));

        try {
            result = "https://duckduckgo.com/html/?q=" + URLEncoder.encode(sub.replace(inputPlaceHolder), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String decodeURL(String decodedURL) {
        String result = null;
        try {
            result = java.net.URLDecoder.decode(decodedURL, String.valueOf(StandardCharsets.UTF_8));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String normalizeLink(String link) {
        if (StringUtils.isEmpty(link)) {
            return "";
        }
        String result;
        if (link.startsWith("http://")) {
            result = link;
        } else if (link.startsWith("www")) {
            result = "http://" + link;
        } else {
            result = "http://www." + link;
        }
        return result;
    }

    public static String createQueryURL(InputCsvModelItem csvItem, String inputPlaceHolder) {
        String result = null;
        if (StringUtils.isEmpty(inputPlaceHolder)) {
            return "";
        }
        StrSubstitutor sub = new StrSubstitutor(valuesMap(csvItem));

        try {
            result = URLEncoder.encode(sub.replace(inputPlaceHolder), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    private static  Map valuesMap(InputCsvModelItem csvItem) {
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
        return valuesMap;
    }

    public static String getSearchValue(InputCsvModelItem csvItem, String inputPlaceHolder) {
        String result = "";
        Map values = valuesMap(csvItem);

        Pattern pattern = Pattern.compile("(?<=\\{)(.*?)(?=\\})");
        Matcher matcher = pattern.matcher(inputPlaceHolder);
        if (matcher.find())
        {
            result = values.get(matcher.group(1)).toString();
        }
        return result;
    }

    public static String getUnmatchedPartOfString(String source) {
        return source.replaceAll("^(?:https?:\\/\\/)?(?:[^@\\/\\n]+@)?(?:www\\.)?([^:\\/?\\n]+)", "");
    }

    public static String extractDomainName(String URL) {
        String result = "";

        Pattern pattern = Pattern.compile("^(?:https?:\\/\\/)?(?:[^@\\/\\n]+@)?(?:www\\.)?([^:\\/?\\n]+)");
        Matcher matcher = pattern.matcher(URL);
        if (matcher.find())
        {
            result = matcher.group(0);
        }
        return result;
    }
}
