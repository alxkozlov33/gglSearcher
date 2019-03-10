package Abstract.OutputModels;

import com.opencsv.bean.CsvBindByName;

public class OutputRegularCSVItem implements IOutputModel {

    @CsvBindByName(column = "GalleryName")
    private String galleryName;

    @CsvBindByName(column = "Website")
    private String website;

    @CsvBindByName(column = "Not sure")
    private String notSure;

    @CsvBindByName(column = "Html page title")
    private String htmlPageTitle;

    public OutputRegularCSVItem(String galleryName, String webSite, String notSureLink, String htmlPageTitle) {
        this.galleryName = galleryName;
        this.website = webSite;
        this.notSure = notSureLink;
        this.htmlPageTitle = htmlPageTitle;
    }

    public String getGalleryName() {
        if (galleryName == null) {
            return "";
        }
        return this.galleryName;
    }
    public String getWebsite() {
        if (website == null) {
            return "";
        }
        return this.website;
    }
    public String getNotSure() {
        if (notSure == null) {
            return "";
        }
        return notSure;
    }

    public String getHtmlPageTittle() {
        if (htmlPageTitle == null) {
            return "";
        }
        return htmlPageTitle;
    }

    public void setGalleryName(String galleryName) {
        this.galleryName = galleryName;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public void setNotSure(String notSure) {
        this.notSure = notSure;
    }

    public void setHtmlPageTitle(String htmlPageTitle) { this.htmlPageTitle = htmlPageTitle; }

    @Override
    public String toCsvRowString() {
        return String.format("\"%s\",\"s\",\"%s\",\"%s\"", galleryName, website, notSure, htmlPageTitle);
    }
}
