package com.sportstracking.strackify.model;

/**
 * strackify: model class for sport
 *
 * @author Nirbhay Ashok Pherwani
 * email: np5318@rit.edu
 * profile: https://nirbhay.me
 */


public class Sport {
    private String sportName;
    private String sportId;
    private String sportFormat;
    private String sportThumbnail;
    private String sportDescription;

    public String getSportName() {
        return sportName;
    }

    public void setSportName(String sportName) {
        this.sportName = sportName;
    }

    public String getSportId() {
        return sportId;
    }

    public void setSportId(String sportId) {
        this.sportId = sportId;
    }

    public String getSportFormat() {
        return sportFormat;
    }

    public void setSportFormat(String sportFormat) {
        this.sportFormat = sportFormat;
    }

    public String getSportThumbnail() {
        return sportThumbnail;
    }

    public void setSportThumbnail(String sportThumbnail) {
        this.sportThumbnail = sportThumbnail;
    }

    public String getSportDescription() {
        return sportDescription;
    }

    public void setSportDescription(String sportDescription) {
        this.sportDescription = sportDescription;
    }
}
