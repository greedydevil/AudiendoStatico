package it.graficheaquilane.audiendo.query;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mysql.jdbc.ResultSet;

import it.graficheaquilane.audiendo.implementation.JdbcMysqlConnection;

public class T_Lavori_Sottolotti {
	
	private String DATABASE;
	private JdbcMysqlConnection mysqlConnection;
	private PreparedStatement preparedStatement;
	private ResultSet resultSet;
	private List<String> result = new ArrayList<>();

	public T_Lavori_Sottolotti(String database) {
		
		this.DATABASE = database;		
		//Mi connetto al db per fare le query sulla tabella
		this.mysqlConnection = new JdbcMysqlConnection(DATABASE);		
		
	}
	
	public List<String> cercaLottiStampa(String query){
		try {
			preparedStatement = mysqlConnection.getConnection().prepareStatement(query);
			//Eseguo la query
		    resultSet = (ResultSet) preparedStatement.executeQuery();
		    
		    //Resituisco tutti i lotti trovati
		    while (resultSet.next()) result.add(resultSet.getString("LOTTO")); 
		   
		    		    
		} catch (SQLException e) {
			System.out.println("T_Lavori_Sottolotti: Errore esecuzione query");
			e.printStackTrace();
		}
	    
		return result;
	}

	public void chiudiConnessione(){
		mysqlConnection.closeConnection();		
	}
	
}
