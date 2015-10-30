package edu.university.database.manager;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBManager {

	private static DBManager dbManager = null;
	Properties properties = null;
	String propertyFileLocation = "C:\\EclipseWorkspaces\\University\\Enrollment\\WebContent\\WEB-INF\\properties\\dbProperties.properties";

	private String userName = "";
	private String password = "";
	String url = "";
	String driverName = "";

	private static Connection connection = null;

	/*
	 * Default constructor
	 */
	public DBManager() {

	}

	/**
	 * This method is called to return a connection to the database. If the
	 * driver has not already been loaded, this method will first load the
	 * driver, then return the newly created connection to the calling class.
	 * 
	 * @return Connection
	 */
	public static Connection getConnection() {

		if (dbManager == null) {
			DBManager dbManager = new DBManager();
			if (dbManager.loadDriver()) {
				System.out.println("SQL Server connection established successfully.");
			} else {
				System.out.println("SQL Server connection failed.  Check log files.");
			}
		}

		return connection;

	}

	/**
	 * This method loads the SQL Server JDBC driver, then returns a connection.
	 * 
	 * @return
	 */
	public boolean loadDriver() {
		boolean success = false;

		if (loadDbProperties(propertyFileLocation)) {
			driverName = properties.getProperty("driverName");
			url = properties.getProperty("url");
			userName = properties.getProperty("userName");
			password = properties.getProperty("password");
			success = true;
		} else {
			System.out.println("Error loading database property file.  See log file.");
		}

		if (success) {
			try {
				Class.forName(driverName);
				connection = DriverManager.getConnection(url, userName, password);
				success = true;
			} catch (ClassNotFoundException e) {
				System.out.println("ClassNotFoundException = " + e.toString());
			} catch (SQLException e) {
				System.out.println("SQLException = " + e.toString());
			}
		}

		return success;
	}

	/**
	 * This method loads the property file that contains the connection
	 * information for the database.
	 * 
	 * @param fileName
	 * @return boolean
	 */
	private boolean loadDbProperties(String fileName) {

		boolean success = false;

		FileInputStream fileInputStream = null;
		properties = new Properties();

		try {
			fileInputStream = new FileInputStream(fileName);
			properties.load(fileInputStream);
			success = true;
		} catch (FileNotFoundException e) {
			System.out.println("FileNotFoundException = " + e.toString());
		} catch (IOException e) {
			System.out.println("IOException = " + e.toString());
		}

		return success;

	}

	/**
	 * This is a main() method for testing this class.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		dbManager.getConnection();

	}

}
