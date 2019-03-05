package Abstract.Specifications;

import Abstract.WebPageObject;
import Models.SearchSettings;

public class WebPageObjectSpecification extends Specification {

    private SearchSettings searchSettings;
    public WebPageObjectSpecification(SearchSettings searchSettings) {
        this.searchSettings = searchSettings;
    }

    @Override
    public boolean IsSatisfiedBy(WebPageObject webPageObject) {
        for (String metaExceptionKeyword : searchSettings.metaTagsExceptions) {
            if (webPageObject.siteDescription.toLowerCase().contains(metaExceptionKeyword.toLowerCase())) {
                return false;
            }
            if (webPageObject.siteKeywords.toLowerCase().contains(metaExceptionKeyword.toLowerCase())) {
                return false;
            }
        }
        return true;
    }
}
