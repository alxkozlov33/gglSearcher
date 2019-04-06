package Abstract.Specifications.Concrete.WebsiteSpecifications;

import Abstract.Specifications.AbstractSpecification;

public class IsInstagramSpecification extends AbstractSpecification<String> {

    @Override
    public boolean isSatisfiedBy(String link) {
        return link.contains("instagram.com") || link.contains("ig.com");
    }
}
