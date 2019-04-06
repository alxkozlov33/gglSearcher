package Abstract.Specifications.Concrete.WebsiteSpecifications;

import Abstract.Specifications.AbstractSpecification;

public class IsTwitterSpecification extends AbstractSpecification<String> {

    @Override
    public boolean isSatisfiedBy(String link) {
        return link.contains("twitter.com") || link.contains("https://t.co");
    }
}
