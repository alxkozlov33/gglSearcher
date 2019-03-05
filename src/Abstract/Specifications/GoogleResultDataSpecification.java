package Abstract.Specifications;

import Abstract.ComparedObject;
import Abstract.SearchResultModels.GoogleSearchResultItem;
import Utils.StrUtils;

public class GoogleResultDataSpecification implements Specification {

    public boolean IsSatisfiedBy(GoogleSearchResultItem webPageObject) {
        String domainName = StrUtils.extractDomainName(webPageObject.link);
        for (String domainNameException: searchSettings.domainExceptions) {
            if (domainName.toLowerCase().contains(domainNameException.toLowerCase())) {
                return false;
            }
        }

        String str = StrUtils.getUnmatchedPartOfString(webPageObject.link);
        for (String urlException: searchSettings.URLExceptions) {
            if (str.toLowerCase().contains(urlException.toLowerCase())) {
                return false;
            }
        }

        for (String topLevelDomainException: searchSettings.topLevelDomainsExceptions) {
            if (link.toLowerCase().contains(topLevelDomainException.toLowerCase())) {
                return false;
            }
        }
        return false;
    }

    @Override
    public boolean IsSatisfiedBy(GoogleSearchResultItem object) {
        return false;
    }
}
