package Models;

import com.opencsv.bean.CsvBindByName;

public class OutputCsvModelItem {

    public OutputCsvModelItem() { }

    @CsvBindByName(column = "GalleryName")
    private String galleryName;

    @CsvBindByName(column = "Website")
    private String website;

    @CsvBindByName(column = "Address")
    private String address;

    public OutputCsvModelItem(String GalleryName, String Website, String Address) {
        this.address = Address;
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
        return this.address;
    }
}
