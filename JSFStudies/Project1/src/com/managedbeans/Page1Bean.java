package com.managedbeans;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;

@ManagedBean
public class Page1Bean {
	
	private String firstName = null;
	private String lastName = null;
	private String country = null;
	private List<String> countryList = null;
	
	/**
	 * Default (no argument) constructor
	 */
	public Page1Bean() {
		
		countryList = new ArrayList<String>();
		countryList.add("United States");
		countryList.add("Brazil");
		countryList.add("France");
		countryList.add("Germany");
		countryList.add("Egypt");
		countryList.add("India");
		
	}
	
	public List<String> getCountryList() {
		return countryList;
	}

	public String getFirstName() {
		return firstName;
	}
	
	public String getLastName() {
		return lastName;
	}

	public String getCountry() {
		return country;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setCountry(String country) {
		this.country = country;
	}

}
