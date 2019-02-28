package Abstract;

import com.opencsv.bean.CsvBindByName;

public abstract class OutputCSVItem extends OutputCSVModelGeoData {

    @CsvBindByName(column = "GalleryName")
    public String galleryName;

    @CsvBindByName(column = "Website")
    public String website;

    @CsvBindByName(column = "NotSure")
    public String notSure;

    public OutputCSVItem(String GalleryName, String Website, String City, String NotSureLink, String Сountry) {
        this.city = City;
        this.galleryName = GalleryName;
        this.website = Website;
        this.notSure = NotSureLink;
        this.country = Сountry;
    }

    public OutputCSVItem(String GalleryName, String Website, String NotSureLink) {
        this.galleryName = GalleryName;
        this.website = Website;
        this.notSure = NotSureLink;
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
}
