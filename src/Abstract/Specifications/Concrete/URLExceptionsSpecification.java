package Abstract.Specifications.Concrete;

import Abstract.SearchResultModels.GoogleSearchResultItem;
import Abstract.Specifications.AbstractSpecification;
import Utils.StrUtils;
import java.util.ArrayList;

public class URLExceptionsSpecification extends AbstractSpecification<GoogleSearchResultItem> {

    public ArrayList<String> URLExceptions;
    public URLExceptionsSpecification(ArrayList<String> URLExceptions) {
        this.URLExceptions = URLExceptions;
    }

    @Override
    public boolean isSatisfiedBy(GoogleSearchResultItem googleSearchResultItem) {
        String str = StrUtils.getUnmatchedPartOfString(googleSearchResultItem.link);
        for (String urlException: URLExceptions) {
            if (str.toLowerCase().contains(urlException.toLowerCase())) {
                return false;
            }
        }
        return true;
    }
}
