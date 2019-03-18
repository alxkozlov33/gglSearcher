package Abstract.Specifications.Concrete;

import Abstract.Models.SearchResultModels.WebPageObject;
import Abstract.Specifications.AbstractSpecification;

import java.util.ArrayList;

public class SpecificWordInPageSpecification extends AbstractSpecification<WebPageObject> {

    private ArrayList<String> specificWordsToSearch;
    public SpecificWordInPageSpecification(ArrayList<String> specificWordsToSearch){
        this.specificWordsToSearch = specificWordsToSearch;
    }

    @Override
    public boolean isSatisfiedBy(WebPageObject webPageObject) {
        if (specificWordsToSearch.size() == 0) {
            return true;
        }

        for (String specificWord : specificWordsToSearch) {
            if (webPageObject.getPagePlainText().toLowerCase().contains(specificWord.toLowerCase())) {
                return true;
            }
        }
        return false;
    }
}
