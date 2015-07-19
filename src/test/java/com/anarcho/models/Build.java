package com.anarcho.models;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;

/**
 * Created by alpa on 7/18/15.
 */
public class Build {

    @SerializedName("build_url")
    private String buildUrl;

    @SerializedName("created_on")
    private BigDecimal createdOn;

    private int id;

    @SerializedName("release_notes")
    private String releaseNotes;

    @SerializedName("version_code")
    private String versionCode;

    @SerializedName("version_name")
    private String versionName;

    public String getBuildUrl() {
        return buildUrl;
    }

    public void setBuildUrl(String buildUrl) {
        this.buildUrl = buildUrl;
    }

    public BigDecimal getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(BigDecimal createdOn) {
        this.createdOn = createdOn;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getReleaseNotes() {
        return releaseNotes;
    }

    public void setReleaseNotes(String releaseNotes) {
        this.releaseNotes = releaseNotes;
    }

    public String getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }
}
