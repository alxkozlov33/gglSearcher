package Models;

import com.opencsv.bean.CsvBindByName;

public class OutputCsvModelItem {

    public OutputCsvModelItem() { }

    @CsvBindByName(column = "GalleryName")
    private String galleryName;

    @CsvBindByName(column = "Website")
    private String website;

    @CsvBindByName(column = "City")
    private String city;

    @CsvBindByName(column = "NotSure")
    private String notSure;

    public OutputCsvModelItem(String GalleryName, String Website, String City, String notSureLink) {
        this.city = City;
        this.galleryName = GalleryName;
        this.website = Website;
        this.notSure = notSureLink;
    }

    public String getGalleryName() {
        return this.galleryName;
    }

    public String getWebsite() {
        return this.website;
    }

    public String getCity() {
        return this.city;
    }

    public String getNotSure() { return notSure; }
}
