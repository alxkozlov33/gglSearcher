package Abstract.Models;

import java.util.ArrayList;

public class SearchSettings {

    public ArrayList<String> domainExceptions;
    public ArrayList<String> URLExceptions;
    public ArrayList<String> metaTagsExceptions;
    public ArrayList<String> topLevelDomainsExceptions;
    public ArrayList<String> keywordsInSearchResults;
    public ArrayList<String> specificWordsInDomainURLs;

    public SearchSettings() {
        domainExceptions = new ArrayList<>();
        URLExceptions = new ArrayList<>();
        metaTagsExceptions = new ArrayList<>();
        topLevelDomainsExceptions = new ArrayList<>();
        keywordsInSearchResults = new ArrayList<>();
        specificWordsInDomainURLs = new ArrayList<>();
    }
}
