package Abstract.Models;

import java.util.ArrayList;

public class SearchSettings {

    public ArrayList<String> ExceptionsForFoundDomains;
    public ArrayList<String> ExceptionsForWordsInDomainURLs;
    public ArrayList<String> MetaTagsExceptions;
    public ArrayList<String> ExceptionsForTopLevelDomains;
    public ArrayList<String> KeywordsForLookingInSearchResults;
    public ArrayList<String> KeywordsForLookingInDomainURLs;

    public SearchSettings() {
        ExceptionsForFoundDomains = new ArrayList<>();
        ExceptionsForWordsInDomainURLs = new ArrayList<>();
        MetaTagsExceptions = new ArrayList<>();
        ExceptionsForTopLevelDomains = new ArrayList<>();
        KeywordsForLookingInSearchResults = new ArrayList<>();
        KeywordsForLookingInDomainURLs = new ArrayList<>();
    }
}
