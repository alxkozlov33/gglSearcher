package Abstract.Specifications.Concrete;

import Abstract.Models.SearchResultModels.GoogleSearchResultItem;
import Abstract.Specifications.AbstractSpecification;
import java.util.ArrayList;

public class TopLevelDomainExceptionsSpecification extends AbstractSpecification<GoogleSearchResultItem> {

    private ArrayList<String> topLevelDomainsExceptions;
    public TopLevelDomainExceptionsSpecification(ArrayList<String> topLevelDomainsExceptions) {
        this.topLevelDomainsExceptions = topLevelDomainsExceptions;
    }

    @Override
    public boolean isSatisfiedBy(GoogleSearchResultItem googleSearchResultItem) {
        if (topLevelDomainsExceptions.size() == 0) {
            return true;
        }
        for (String topLevelDomainException: topLevelDomainsExceptions) {
            if (googleSearchResultItem.getLink().toLowerCase().contains(topLevelDomainException.toLowerCase())) {
                return false;
            }
        }
        return true;
    }
}
