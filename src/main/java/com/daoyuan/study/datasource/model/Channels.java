package com.daoyuan.study.datasource.model;

import java.util.Date;

public class Channels {
    private Long id;

    private String name;

    private Integer appId;

    private String levelType;

    private Long aid;

    private Long bid;

    private Long cid;

    private String linkName;

    private String linkMobile;

    private String telephone;

    private String provinceScope;

    private String cityScope;

    private String districtScope;

    private Integer province;

    private Integer city;

    private Integer district;

    private String street;

    private Date createdAt;

    private Date updatedAt;

    private String nameEncrypt;

    private String linkManEncrypt;

    private String linkMobileEncrypt;

    private String telephoneEncrypt;

    private String streetEncrypt;

    private Boolean deleteFlag;

    private Byte state;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getAppId() {
        return appId;
    }

    public void setAppId(Integer appId) {
        this.appId = appId;
    }

    public String getLevelType() {
        return levelType;
    }

    public void setLevelType(String levelType) {
        this.levelType = levelType == null ? null : levelType.trim();
    }

    public Long getAid() {
        return aid;
    }

    public void setAid(Long aid) {
        this.aid = aid;
    }

    public Long getBid() {
        return bid;
    }

    public void setBid(Long bid) {
        this.bid = bid;
    }

    public Long getCid() {
        return cid;
    }

    public void setCid(Long cid) {
        this.cid = cid;
    }

    public String getLinkName() {
        return linkName;
    }

    public void setLinkName(String linkName) {
        this.linkName = linkName == null ? null : linkName.trim();
    }

    public String getLinkMobile() {
        return linkMobile;
    }

    public void setLinkMobile(String linkMobile) {
        this.linkMobile = linkMobile == null ? null : linkMobile.trim();
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone == null ? null : telephone.trim();
    }

    public String getProvinceScope() {
        return provinceScope;
    }

    public void setProvinceScope(String provinceScope) {
        this.provinceScope = provinceScope == null ? null : provinceScope.trim();
    }

    public String getCityScope() {
        return cityScope;
    }

    public void setCityScope(String cityScope) {
        this.cityScope = cityScope == null ? null : cityScope.trim();
    }

    public String getDistrictScope() {
        return districtScope;
    }

    public void setDistrictScope(String districtScope) {
        this.districtScope = districtScope == null ? null : districtScope.trim();
    }

    public Integer getProvince() {
        return province;
    }

    public void setProvince(Integer province) {
        this.province = province;
    }

    public Integer getCity() {
        return city;
    }

    public void setCity(Integer city) {
        this.city = city;
    }

    public Integer getDistrict() {
        return district;
    }

    public void setDistrict(Integer district) {
        this.district = district;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street == null ? null : street.trim();
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getNameEncrypt() {
        return nameEncrypt;
    }

    public void setNameEncrypt(String nameEncrypt) {
        this.nameEncrypt = nameEncrypt == null ? null : nameEncrypt.trim();
    }

    public String getLinkManEncrypt() {
        return linkManEncrypt;
    }

    public void setLinkManEncrypt(String linkManEncrypt) {
        this.linkManEncrypt = linkManEncrypt == null ? null : linkManEncrypt.trim();
    }

    public String getLinkMobileEncrypt() {
        return linkMobileEncrypt;
    }

    public void setLinkMobileEncrypt(String linkMobileEncrypt) {
        this.linkMobileEncrypt = linkMobileEncrypt == null ? null : linkMobileEncrypt.trim();
    }

    public String getTelephoneEncrypt() {
        return telephoneEncrypt;
    }

    public void setTelephoneEncrypt(String telephoneEncrypt) {
        this.telephoneEncrypt = telephoneEncrypt == null ? null : telephoneEncrypt.trim();
    }

    public String getStreetEncrypt() {
        return streetEncrypt;
    }

    public void setStreetEncrypt(String streetEncrypt) {
        this.streetEncrypt = streetEncrypt == null ? null : streetEncrypt.trim();
    }

    public Boolean getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Boolean deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public Byte getState() {
        return state;
    }

    public void setState(Byte state) {
        this.state = state;
    }
}