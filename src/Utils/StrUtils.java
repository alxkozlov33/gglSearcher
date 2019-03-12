package Utils;

import Abstract.Models.InputModels.InputCsvModelItem;
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
        String queryTerm;
        if (csvItem != null) {
            StrSubstitutor sub = new StrSubstitutor(valuesMap(csvItem));
            queryTerm = sub.replace(inputPlaceHolder);
        }
        else {
            queryTerm = inputPlaceHolder;
        }

        try {
            result = "https://www.google.com/search?q=" +
                    URLEncoder.encode(queryTerm, "UTF-8") +
                    "&pws=0&gl=us&gws_rd=cr&num=15";
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String normalizeGoogleLink(String link) {
        if (link.startsWith("http://") || link.startsWith("https://")) {
            return link;
        } else {
            return "http://www.google.com"+link;
        }
    }

    public static boolean isPlaceholderHasSubstituteTerms(String placeholder) {
        String pattern = "\\$\\{column[A-z]\\}";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(placeholder);
        return m.find();
    }

    public static boolean isStringContainsExtraSymbols(String value) {
        return value.contains("\"") || value.contains("{") || value.contains("}") || value.contains("%") ||
                value.contains("!") || value.contains("@") || value.contains("#") || value.contains("$") ||
                value.contains("^") || value.contains("&") || value.contains("*") || value.contains("(") ||
                value.contains(")") || value.contains("=") || value.contains("+");
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

    public static String clearPlaceholderFromCSVColumnsTerms(String placeholder){
        return placeholder.replaceAll("\\$\\{column[A-z]\\}", "");
    }

    public static String clearLink(String link) {
        if (StringUtils.isEmpty(link)) {
            return "";
        }
        link = link.replace("http://www.google.com/url?url=", "");
        if (link.startsWith("www")) {
            link = "http://" + link;
        }
        if (link.startsWith("/url")) {
            link = link.substring(link.indexOf("=") + 1);
        }
        if (link.indexOf("&") > 0) {
            link = link.substring(0, link.indexOf("&"));
        }
        return link;
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
        valuesMap.put("columnA", replaceCharacters(csvItem.getColumnA()));
        valuesMap.put("columnB", replaceCharacters(csvItem.getColumnB()));
        valuesMap.put("columnC", replaceCharacters(csvItem.getColumnC()));
        valuesMap.put("columnD", replaceCharacters(csvItem.getColumnD()));
        valuesMap.put("columnE", replaceCharacters(csvItem.getColumnE()));
        valuesMap.put("columnF", replaceCharacters(csvItem.getColumnF()));
        valuesMap.put("columnG", replaceCharacters(csvItem.getColumnG()));
        valuesMap.put("columnH", replaceCharacters(csvItem.getColumnH()));
        valuesMap.put("columnI", replaceCharacters(csvItem.getColumnI()));
        valuesMap.put("columnJ", replaceCharacters(csvItem.getColumnJ()));
        valuesMap.put("columnK", replaceCharacters(csvItem.getColumnK()));
        valuesMap.put("columnL", replaceCharacters(csvItem.getColumnL()));
        valuesMap.put("columnM", replaceCharacters(csvItem.getColumnM()));
        valuesMap.put("columnN", replaceCharacters(csvItem.getColumnN()));
        valuesMap.put("columnO", replaceCharacters(csvItem.getColumnO()));
        return valuesMap;
    }

    private static String replaceCharacters(String value) {
        if (StringUtils.isEmpty(value)) {
            return value;
        }
        else {
            return value.replaceAll("[^a-zA-Z0-9\\s]", "");
        }
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


    public static boolean isProxyGrabbed(String response) {
        if (response.isEmpty()) {
            return false;
        }
        Pattern pattern = Pattern.compile("^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5]):[0-9]+$");
        Matcher matcher = pattern.matcher(response);
        if (matcher.find())
        {
            return true;
        }
        return false;
    }
}
