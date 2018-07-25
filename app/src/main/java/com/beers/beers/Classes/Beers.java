package com.beers.beers.Classes;

import java.io.Serializable;

public class Beers implements Serializable {

    private String id;
    private String id_favorite;
    private String name;
    private String tagline;
    private String image_url;
    private String description;
    private String first_brewed;
    private String abv;
    private String ph;
    private String mac;

    public Beers() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId_favorite() {
        return id_favorite;
    }

    public void setId_favorite(String id_favorite) {
        this.id_favorite = id_favorite;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFirst_brewed() {
        return first_brewed;
    }

    public void setFirst_brewed(String first_brewed) {
        this.first_brewed = first_brewed;
    }

    public String getAbv() {
        return abv;
    }

    public void setAbv(String abv) {
        this.abv = abv;
    }

    public String getPh() {
        return ph;
    }

    public void setPh(String ph) {
        this.ph = ph;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }


    public Beers(String id, String id_favorite, String name, String tagline, String image_url, String description, String first_brewed, String abv, String ph, String mac) {
        this.id = id;
        this.id_favorite = id_favorite;
        this.name = name;
        this.tagline = tagline;
        this.image_url = image_url;
        this.description = description;
        this.first_brewed = first_brewed;
        this.abv = abv;
        this.ph = ph;
        this.mac = mac;
    }
}
