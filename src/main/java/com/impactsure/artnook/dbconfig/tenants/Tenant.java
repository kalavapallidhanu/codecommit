package com.impactsure.artnook.dbconfig.tenants;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Tenant {
    @Id
    private String id;
    private String url;
    private String username;
    
    private String logo;
    
    @Column(name="pwd")
    private String password;
   
   
    
    
    @Column(name="show_banner")
    private Short showBanner;
    

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	
	public Short getShowBanner() {
		return showBanner;
	}

	public void setShowBanner(Short showBanner) {
		this.showBanner = showBanner;
	}


    
   
    
}
