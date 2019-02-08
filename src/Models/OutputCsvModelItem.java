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

    public OutputCsvModelItem(String GalleryName, String Website, String City) {
        this.city = City;
        this.galleryName = GalleryName;
        this.website = Website;
    }

    public String getGalleryName() {
        return this.galleryName;
    }

    public String getWebsite() {
        return this.website;
    }

    public String getAddress() {
        return this.city;
    }

    public String getNotSure() { return notSure; }
}
