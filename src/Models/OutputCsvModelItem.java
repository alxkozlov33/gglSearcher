package Models;

import com.opencsv.bean.CsvBindByName;

public class OutputCsvModelItem {

    public OutputCsvModelItem() {
    }

    @CsvBindByName(column = "GalleryName")
    private String galleryName;

    @CsvBindByName(column = "Website")
    private String website;

    @CsvBindByName(column = "City")
    private String city;

    @CsvBindByName(column = "NotSure")
    private String notSure;

    @CsvBindByName(column = "Country")
    private String country;

    public OutputCsvModelItem(String GalleryName, String Website, String City, String notSureLink, String country) {
        this.city = City;
        this.galleryName = GalleryName;
        this.website = Website;
        this.notSure = notSureLink;
        this.country = country;
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

    public String getCity() {
        if (city == null) {
            return "";
        }
        return this.city;
    }

    public String getNotSure() {
        if (notSure == null) {
            return "";
        }
        return notSure;
    }

    public String getCountry() {
        if (country == null) {
            return "";
        }
        return country;
    }
}
