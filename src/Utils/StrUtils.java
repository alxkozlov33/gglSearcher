package Utils;

import Abstract.Models.InputModels.InputCsvModelItem;
import org.apache.commons.lang.StringUtils;
import org.tinylog.Logger;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StrUtils {

    public synchronized static String extractWebSiteFromURL(String url) {
        String webSiteUrlRegex = "(http://www\\.|https://www\\.|http://|https://)?[a-z0-9]+([\\-.][a-z0-9]+)*\\.[a-z]{2,5}(:[0-9]{1,5})?";
        Pattern pattern = Pattern.compile(webSiteUrlRegex);
        Matcher matcher = pattern.matcher(url);
        if (matcher.find()) {
            return matcher.group(0);
        }
        return url;
    }

    public static String createUrlForMultipleSearch(InputCsvModelItem csvItem, String inputPlaceHolder) {
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
                    "&pws=0&gl=us&gws_rd=cr&num=100";
        } catch (UnsupportedEncodingException e) {
            Logger.tag("SYSTEM").error(e);
        }
        return result;
    }

    public static String createSearchTermForMultipleSearch(InputCsvModelItem csvItem, String inputPlaceHolder) {
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
        return queryTerm;
    }

    public static String createUrlForSingleSearch(String inputPlaceHolder) {
        String result = null;
        if (StringUtils.isEmpty(inputPlaceHolder)) {
            return "";
        }

        try {
            result = "https://www.google.com/search?q=" +
                    URLEncoder.encode(inputPlaceHolder, "UTF-8") +
                    "&pws=0&gl=us&gws_rd=cr&num=150";
        } catch (UnsupportedEncodingException e) {
            Logger.tag("SYSTEM").error(e);
        }
        return result;
    }

    public static String encodeStringToUTF8(String inputString) {
        String result = null;
        try {
            result = URLEncoder.encode(inputString, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            Logger.tag("SYSTEM").error(e);
        }
        return result;
    }

    public static String createUrlForGoogleMaps(String inputPlaceHolder) {
        String result = null;
        if (StringUtils.isEmpty(inputPlaceHolder)) {
            return "";
        }

        String pattern = "(?<=&q=)(.*)(?=&)";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(inputPlaceHolder);
        if (m.find()) {
            try {
                result = "https://maps.google.com/maps?q=" +
                        URLEncoder.encode(m.group(0), "UTF-8") +
                        "&pws=0&gl=us&gws_rd=cr&num=150";
            } catch (UnsupportedEncodingException e) {
                Logger.tag("SYSTEM").error(e);
            }
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

    public static String getCityFromAddress(String value) {
        if (value.contains(",")){
            return value.split(",")[0];
        }
        return value;
    }

    public static String getCountryFromAddress(String value) {
        if (value.contains(",")){
            return value.split(",")[1];
        }
        return value;
    }

    public static boolean isPlaceholderHasSubstituteTerms(String placeholder) {
        String pattern = "\\{column[A-z]}";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(placeholder);
        return m.find();
    }

    private static Map valuesMap(InputCsvModelItem csvItem) {
        Map valuesMap = new HashMap();
        valuesMap.put("columna", replaceCharacters(csvItem.getColumnA()));
        valuesMap.put("columnb", replaceCharacters(csvItem.getColumnB()));
        valuesMap.put("columnc", replaceCharacters(csvItem.getColumnC()));
        valuesMap.put("columnd", replaceCharacters(csvItem.getColumnD()));
        valuesMap.put("columne", replaceCharacters(csvItem.getColumnE()));
        valuesMap.put("columnf", replaceCharacters(csvItem.getColumnF()));
        valuesMap.put("columng", replaceCharacters(csvItem.getColumnG()));
        valuesMap.put("columnh", replaceCharacters(csvItem.getColumnH()));
        valuesMap.put("columni", replaceCharacters(csvItem.getColumnI()));
        valuesMap.put("columnj", replaceCharacters(csvItem.getColumnJ()));
        valuesMap.put("columnk", replaceCharacters(csvItem.getColumnK()));
        valuesMap.put("columnl", replaceCharacters(csvItem.getColumnL()));
        valuesMap.put("columnm", replaceCharacters(csvItem.getColumnM()));
        valuesMap.put("columnn", replaceCharacters(csvItem.getColumnN()));
        valuesMap.put("columno", replaceCharacters(csvItem.getColumnO()));
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

    public static String cutStringFromEnd(String string, int length) {
        String result = "";
        if (string.length() > length)
        {
            result = ".."+string.substring(string.length() - length);
        }
        return result;
    }
}
