package Abstract;

import com.opencsv.bean.CsvBindByName;

public abstract class OutputCSVItem extends OutputCSVModelGeoData {

    @CsvBindByName(column = "GalleryName")
    private String galleryName;

    @CsvBindByName(column = "Website")
    private String website;

    @CsvBindByName(column = "NotSure")
    private String notSure;

    public OutputCSVItem(String GalleryName, String Website, String City, String notSureLink, String Country) {
        super(City, Country);
        this.galleryName = GalleryName;
        this.website = Website;
        this.notSure = notSureLink;
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

    public void setGalleryName(String galleryName) {
        this.galleryName = galleryName;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public void setNotSure(String notSure) {
        this.notSure = notSure;
    }
}
