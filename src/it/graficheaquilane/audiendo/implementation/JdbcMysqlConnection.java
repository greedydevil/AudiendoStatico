package it.graficheaquilane.audiendo.implementation;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JdbcMysqlConnection {

	private static String URL;
	private static String USERNAME = "nuovaipadb";
	private static String PASSWORD = "nuovaipadb";
	private Connection connection = null;
	
	public JdbcMysqlConnection(String db) {
		
		switch(db){
			case "graficheaquilane":
				URL = "jdbc:mysql://192.168.1.222/graficheaquilane";
			break;
			
			case "nuovaipa":
				URL = "jdbc:mysql://192.168.1.222/intranet";
			break;
			
			case "cesidioBello":
				URL = "jdbc:mysql://192.168.1.222/cesidioBello";
			break;
			
			default:
				URL = "jdbc:mysql://192.168.1.222/cesidioBello";
			break;
						
		}
				
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.out.println("JdbcMysqlConnection - Error DriverManager");
			e.printStackTrace();
		}
			
		try {
			connection = (Connection) DriverManager.getConnection(URL, USERNAME, PASSWORD);
		} catch (SQLException e) {
			System.out.println("JdbcMysqlConnection - Error Connection");
			e.printStackTrace();
		}
	
	}

	public Connection getConnection() {
		return this.connection;
	} 
	
	public void closeConnection(){
		try {
			connection.close();
		} catch (SQLException e) {
			System.out.println("JdbcMysqlConnection - Errore nella chiusura della connessione");
			e.printStackTrace();
		}
	}
 
}
	

