package Abstract.Specifications.Concrete;

import Abstract.Models.SearchResultModels.GoogleSearchResultItem;
import Abstract.Specifications.AbstractSpecification;
import Utils.StrUtils;

import java.util.ArrayList;

public class URLSpecificWordsSearchSpecification extends AbstractSpecification<GoogleSearchResultItem> {

    private ArrayList<String> URLSpecificWords;
    public URLSpecificWordsSearchSpecification(ArrayList<String> URLSpecificWords) {
        this.URLSpecificWords = URLSpecificWords;
    }

    @Override
    public boolean isSatisfiedBy(GoogleSearchResultItem googleSearchResultItem) {
        if (URLSpecificWords.size() == 0) {
            return true;
        }
        String str = StrUtils.getUnmatchedPartOfString(googleSearchResultItem.getLink());
        for (String urlSpecificWords: URLSpecificWords) {
            if (str.toLowerCase().contains(urlSpecificWords.toLowerCase())) {
                return true;
            }
        }
        return false;
    }
}
