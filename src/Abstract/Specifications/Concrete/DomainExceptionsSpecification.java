package Abstract.Specifications.Concrete;

import Abstract.SearchResultModels.GoogleSearchResultItem;
import Abstract.Specifications.AbstractSpecification;
import Utils.StrUtils;
import java.util.ArrayList;

public class DomainExceptionsSpecification extends AbstractSpecification<GoogleSearchResultItem> {

    private ArrayList<String> domainExceptions;
    public DomainExceptionsSpecification(ArrayList<String> domainExceptions) {
        this.domainExceptions = domainExceptions;
    }

    @Override
    public boolean isSatisfiedBy(GoogleSearchResultItem googleSearchResultItem) {
        String domainName = StrUtils.extractDomainName(googleSearchResultItem.getLink());
        for (String domainNameException: domainExceptions) {
            if (domainName.toLowerCase().contains(domainNameException.toLowerCase())) {
                return false;
            }
        }
        return false;
    }
}
