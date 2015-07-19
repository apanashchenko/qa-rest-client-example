package com.anarcho.models;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;

/**
 * Created by alpa on 7/18/15.
 */
public class App {

    @SerializedName("app_key")
    private String appKey;

    @SerializedName("app_type")
    private String appType;

    @SerializedName("created_on")
    private BigDecimal createdOn;

    @SerializedName("icon_url")
    private String iconUrl;

    private String name;
    private String permission;


    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getAppType() {
        return appType;
    }

    public void setAppType(String appType) {
        this.appType = appType;
    }

    public BigDecimal getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(BigDecimal createdOn) {
        this.createdOn = createdOn;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }
}
