package Abstract.Specifications.Concrete.WebsiteSpecifications;

import Abstract.Specifications.AbstractSpecification;

public class IsFacebookSpecification extends AbstractSpecification<String> {

    @Override
    public boolean isSatisfiedBy(String link) {
        return link.contains("facebook.com") || link.contains("fb.com");
    }
}
