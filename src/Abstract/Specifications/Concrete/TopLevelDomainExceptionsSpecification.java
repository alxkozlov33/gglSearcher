package Abstract.Specifications.Concrete;

import Abstract.SearchResultModels.GoogleSearchResultItem;
import Abstract.Specifications.AbstractSpecification;
import java.util.ArrayList;

public class TopLevelDomainExceptionsSpecification extends AbstractSpecification<GoogleSearchResultItem> {

    private ArrayList<String> topLevelDomainsExceptions;
    public TopLevelDomainExceptionsSpecification(ArrayList<String> topLevelDomainsExceptions) {
        this.topLevelDomainsExceptions = topLevelDomainsExceptions;
    }

    @Override
    public boolean isSatisfiedBy(GoogleSearchResultItem googleSearchResultItem) {
        for (String topLevelDomainException: topLevelDomainsExceptions) {
            if (googleSearchResultItem.getLink().toLowerCase().contains(topLevelDomainException.toLowerCase())) {
                return false;
            }
        }
        return true;
    }
}
