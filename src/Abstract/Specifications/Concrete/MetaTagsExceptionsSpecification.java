package Abstract.Specifications.Concrete;

import Abstract.Specifications.AbstractSpecification;
import Abstract.Models.SearchResultModels.WebPageObject;

import java.util.ArrayList;

public class MetaTagsExceptionsSpecification extends AbstractSpecification<WebPageObject> {

    private ArrayList<String> metaTagsExceptions;
    public MetaTagsExceptionsSpecification(ArrayList<String> metaTagsExceptions){
        this.metaTagsExceptions = metaTagsExceptions;
    }

    @Override
    public boolean isSatisfiedBy(WebPageObject webPageObject) {
        for (String metaExceptionKeyword : metaTagsExceptions) {
            if (webPageObject.getSiteDescription().toLowerCase().contains(metaExceptionKeyword.toLowerCase())) {
                return false;
            }
            if (webPageObject.getSiteKeywords().toLowerCase().contains(metaExceptionKeyword.toLowerCase())) {
                return false;
            }
        }
        return true;
    }
}
