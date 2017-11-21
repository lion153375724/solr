package com.learn.solr;

import org.apache.solr.client.solrj.beans.Field;

public class Business {
	private int businessId;
	private String name;
	private String mainProduct;
	private String businessModel;
	private String companyProfile;
	private String shopsName;
	private String shopsDesc;
	private String shopsUrl;
	private String provinceId;
	private String cityId;
	private String areaId;
	private String address;
	private String level;
	private String mobile;
	private String status;// 1为已经认证，0未认证，2已驳回
	private String marketId;
	private String marketName;

	public int getBusinessId() {
		return businessId;
	}

	@Field
	public void setBusinessId(int businessId) {
		this.businessId = businessId;
	}

	public String getName() {
		return name;
	}

	@Field
	public void setName(String name) {
		this.name = name;
	}

	public String getMainProduct() {
		return mainProduct;
	}

	@Field
	public void setMainProduct(String mainProduct) {
		this.mainProduct = mainProduct;
	}

	public String getBusinessModel() {
		return businessModel;
	}

	@Field
	public void setBusinessModel(String businessModel) {
		this.businessModel = businessModel;
	}

	public String getCompanyProfile() {
		return companyProfile;
	}

	@Field
	public void setCompanyProfile(String companyProfile) {
		this.companyProfile = companyProfile;
	}

	public String getShopsName() {
		return shopsName;
	}

	@Field
	public void setShopsName(String shopsName) {
		this.shopsName = shopsName;
	}

	public String getShopsDesc() {
		return shopsDesc;
	}

	@Field
	public void setShopsDesc(String shopsDesc) {
		this.shopsDesc = shopsDesc;
	}

	public String getShopsUrl() {
		return shopsUrl;
	}

	@Field
	public void setShopsUrl(String shopsUrl) {
		this.shopsUrl = shopsUrl;
	}

	public String getProvinceId() {
		return provinceId;
	}

	@Field
	public void setProvinceId(String provinceId) {
		this.provinceId = provinceId;
	}

	public String getCityId() {
		return cityId;
	}

	@Field
	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public String getAreaId() {
		return areaId;
	}

	@Field
	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}

	public String getAddress() {
		return address;
	}

	@Field
	public void setAddress(String address) {
		this.address = address;
	}

	public String getLevel() {
		return level;
	}

	@Field
	public void setLevel(String level) {
		this.level = level;
	}

	public String getMobile() {
		return mobile;
	}

	@Field
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getStatus() {
		return status;
	}

	@Field
	public void setStatus(String status) {
		this.status = status;
	}

	public String getMarketId() {
		return marketId;
	}

	@Field
	public void setMarketId(String marketId) {
		this.marketId = marketId;
	}

	public String getMarketName() {
		return marketName;
	}

	@Field
	public void setMarketName(String marketName) {
		this.marketName = marketName;
	}

}
