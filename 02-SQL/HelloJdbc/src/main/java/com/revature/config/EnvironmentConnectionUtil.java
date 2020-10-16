package com.revature.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class EnvironmentConnectionUtil {
	private String url = System.getenv("url");
	private String username = System.getenv("username");
	private String password = System.getenv("password");
	private static EnvironmentConnectionUtil instance;
	
	{
		try {
			FileInputStream f = new FileInputStream("src/main/resources/connection.properties");
			Properties p = new Properties();
			
			p.load(f);
			username = p.getProperty("username");
			password = p.getProperty("password");
			url = p.getProperty("url");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private EnvironmentConnectionUtil() {
		
	}
	public static EnvironmentConnectionUtil getInstance() {
		if(instance == null) {
			instance = new EnvironmentConnectionUtil();
		}
		
		return instance;
	}
	
	
	
	public Connection getConnection() throws SQLException{
		return DriverManager.getConnection(url, username, password);
	}
}
