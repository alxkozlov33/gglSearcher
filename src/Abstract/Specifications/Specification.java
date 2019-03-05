package Abstract.Specifications;

import Abstract.ComparedObject;
import Abstract.SearchResultModels.GoogleSearchResultItem;
import Abstract.WebPageObject;

public abstract class Specification {
    abstract boolean IsSatisfiedBy(ComparedObject object);
}
