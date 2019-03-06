package Abstract;

public class WebPageObject {

    public WebPageObject(String siteDescription, String siteKeywords, String siteName) {
        this.siteDescription = siteDescription;
        this.siteKeywords = siteKeywords;
        this.siteName = siteName;
    }

    private String siteDescription;
    private String siteKeywords;
    private String siteName;

    public String getSiteDescription() {
        return siteDescription;
    }

    public String getSiteKeywords() {
        return siteKeywords;
    }

    public String getSiteName() {
        return siteName;
    }
}
