package org.inexture.wikicrawler.configuration;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {
	
		static Properties prop;
		public static Connection getConnection() throws ClassNotFoundException, SQLException{
			 try {
				 prop=new Properties();
				 InputStream input = DatabaseConnection.class.getClassLoader().getResourceAsStream("dbConnection.properties");
				 prop.load(input);
				 input.close();
			 } catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Class.forName( prop.getProperty("driverClassName") );  
			Connection connection = DriverManager.getConnection( prop.getProperty("url"), prop.getProperty("username"), prop.getProperty("password"));
			return connection;  
		}
	}


