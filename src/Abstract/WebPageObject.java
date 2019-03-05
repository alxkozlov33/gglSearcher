package Abstract;

public class WebPageObject extends ComparedObject {

    public WebPageObject(String siteDescription, String siteKeywords, String siteName) {
        this.siteDescription = siteDescription;
        this.siteKeywords = siteKeywords;
        this.siteName = siteName;
    }

    public String siteDescription;
    public String siteKeywords;
    public String siteName;
}
