package com.revature.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PlainTextConnectionUtil {
	// url for jdbc -> jdbc:postgresql://hostendpoint:port/dbName?currentSchema=schema
	// default schema is public; this can be changed
	
	private final String url = "jdbc:postgresql://database-1.c2fwjr2ekpz7.us-east-1.rds.amazonaws.com"
			+ ":5432/postgres?currentSchema=jdbc_schema";
	private final String username = "jdbc_worker";
	private final String password = "wasspord";
	
	
	// singleton instance, for testability
	// Mockito can't mock anything that's static
	private static PlainTextConnectionUtil instance;
	
	private PlainTextConnectionUtil() {
		super();
	}
	
	// create a single connector to the database
	public static PlainTextConnectionUtil getInstance() {
		if(instance == null) {
			instance = new PlainTextConnectionUtil();
		}
		
		return instance;
	}
	
	
	// connect
	public Connection getConnection() throws SQLException {
		return DriverManager.getConnection(url, username, password);
	}
}
