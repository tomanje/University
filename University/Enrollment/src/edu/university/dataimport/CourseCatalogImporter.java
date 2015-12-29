package edu.university.dataimport;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.StringTokenizer;

import edu.university.database.manager.DBManager;

public class CourseCatalogImporter {

	private static final String COURSE_CATALOG_CSV_FILE_NAME = "C:\\EclipseWorkspaces\\University\\ImportDataFiles\\CourseCatalog.csv";
	private static BufferedReader br = null;
	private String insertStatement = "INSERT INTO CourseCatalog (CourseNumber, CourseTitle, CourseDescription, CreditHours, College, Classification, PreReqCourseNbr) VALUES (?, ?, ?, ?, ?, ?, ?)";
	private String selectStatement = "SELECT CourseNumber FROM CourseCatalog where CourseNumber = ?";
	private String deleteStatement = "DELETE FROM CourseCatalog WHERE CourseNumber = ?";
	
	private Connection connection = null; 
	private PreparedStatement insertPs = null;
	private PreparedStatement selectPs = null;
	private PreparedStatement deletePs = null;

	/*
	 * Default constructor
	 */
	public CourseCatalogImporter() {
		DBManager dbManager = DBManager.getDBManager();
		connection = dbManager.getConnection();
		try {
			insertPs = connection.prepareStatement(insertStatement);
			selectPs = connection.prepareStatement(selectStatement);
			deletePs = connection.prepareStatement(deleteStatement);
		} catch (SQLException e) {
			System.out.println("SQLException = " + e.toString());
		}
	}
	
	/**
	 * This method processes the import of the Course Catalog file into the database table. 
	 */
	public void processFile() {
		
		String line = "";
		boolean notFirst = false;

		try {
			// FileReader reads text files in the default encoding.
			br = new BufferedReader(new FileReader(COURSE_CATALOG_CSV_FILE_NAME));

			while ((line = br.readLine()) != null) {
				System.out.println(line);
				if(notFirst) {
					insertRecord(line);
				}
				notFirst = true;
			}

			// Always close files.
			br.close();
		} catch (FileNotFoundException e) {
			System.out.println("Unable to open file '" + COURSE_CATALOG_CSV_FILE_NAME + "'");
			System.out.println("FileNotFoundException = " + e.toString());
		} catch (IOException e) {
			System.out.println("Error reading file '" + COURSE_CATALOG_CSV_FILE_NAME + "'");
			System.out.println("IOException = " + e.toString());
		}
		
	}
	
	/**
	 * This method adds the row to the database table
	 * @param record
	 */
	private void insertRecord(String record) {
		
		deleteExistingRecord(record);
		
		StringTokenizer st = new StringTokenizer(record, ",");
		int x = 1;
		
		while(st.hasMoreElements()) {
			try {
				if(x == 4) {
					insertPs.setInt(x, new Integer(st.nextToken()).intValue());
				}
				else {
					insertPs.setString(x, st.nextToken());
				}
			} catch (SQLException e) {
				System.out.println("SQLException adding parameters = " + e.toString());
			}
			x++;
		}
		
		try {
			insertPs.executeUpdate();
			System.out.println("CourseCatalog record added");
			insertPs.close();
		} catch (SQLException e) {
			System.out.println("SQLException inserting record = " + e.toString());
		}
	
	}
	
	/**
	 * This method deletes an existing course record from the CourseCatalog
	 * if one already exists. 
	 * @param CourseNumber
	 */
	private void deleteExistingRecord(String record) {
		
		StringTokenizer st = new StringTokenizer(record, ",");
		String courseNumber = st.nextToken();
		
		if(exists(courseNumber)) {
			
			try {
				deletePs.setString(1, courseNumber);
				int i = selectPs.executeUpdate();
				System.out.println("CourseCatalog record deleted.  Return code = " + i);
			} catch (SQLException e) {
				System.out.println("SQLException closing the prepared statement = " + e.toString());
			}
			
		}
		
	}
	
	/**
	 * This method checks for the existence of the CourseNumber in the 
	 * Course catalog table. 
	 * @param CourseNumber
	 * @return boolean
	 */
	private boolean exists(String courseNumber) {
		
		boolean recordFound = false;
		
		try {
			selectPs.setString(1, courseNumber);
			ResultSet rs = selectPs.executeQuery();
			if(rs != null && rs.next()) {
				recordFound = true;
			}
		} catch (SQLException e) {
			System.out.println("exists() SQLException closing the prepared statement = " + e.toString());
		}
		
		return recordFound;
	}

	/**
	 * main() method for testing.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		CourseCatalogImporter CourseCatalogImporter = new CourseCatalogImporter();
		CourseCatalogImporter.processFile();

	}

}
