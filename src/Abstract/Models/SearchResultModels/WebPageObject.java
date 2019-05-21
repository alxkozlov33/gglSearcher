package Abstract.Models.SearchResultModels;

import org.tinylog.Logger;

public class WebPageObject {

    public WebPageObject(String siteDescription, String siteKeywords, String siteName, String pagePlainText) {
        this.siteDescription = siteDescription;
        this.siteKeywords = siteKeywords;
        this.siteName = siteName;
        this.pagePlainText = pagePlainText;
    }

    private String siteDescription;
    private String siteKeywords;
    private String siteName;
    private String pagePlainText;

    public String getSiteDescription() {
        return siteDescription;
    }

    public String getSiteKeywords() {
        return siteKeywords;
    }

    public String getSiteName() {
        return siteName;
    }

    public String getPagePlainText() {
        return pagePlainText;
    }
}
