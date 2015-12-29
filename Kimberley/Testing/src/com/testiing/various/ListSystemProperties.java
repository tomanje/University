package com.testiing.various;

import java.util.Enumeration;
import java.util.Properties;

public class ListSystemProperties {

	public static void main(String[] args) {
		
		ListSystemProperties lsp = new ListSystemProperties();
		lsp.listProperties();

	}
	
	public void listProperties() {
	
		Properties properties = System.getProperties();
		Enumeration e = properties.keys();

		while(e.hasMoreElements()) {
			String key = (String) e.nextElement();
			String value = properties.getProperty(key);
			System.out.println(key + " = " + value);
		}
		
	}

}
