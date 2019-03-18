package Abstract.Specifications.Concrete;

import Abstract.Models.SearchResultModels.GoogleSearchResultItem;
import Abstract.Specifications.AbstractSpecification;
import Utils.StrUtils;
import java.util.ArrayList;

public class URLExceptionsSpecification extends AbstractSpecification<GoogleSearchResultItem> {

    private ArrayList<String> URLExceptions;
    public URLExceptionsSpecification(ArrayList<String> URLExceptions) {
        this.URLExceptions = URLExceptions;
    }

    @Override
    public boolean isSatisfiedBy(GoogleSearchResultItem googleSearchResultItem) {
        if (URLExceptions.size() == 0) {
            return true;
        }
        String str = StrUtils.getUnmatchedPartOfString(googleSearchResultItem.getLink());
        for (String urlException: URLExceptions) {
            if (str.toLowerCase().contains(urlException.toLowerCase())) {
                return false;
            }
        }
        return true;
    }
}
