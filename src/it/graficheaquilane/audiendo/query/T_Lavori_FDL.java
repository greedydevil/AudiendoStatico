package it.graficheaquilane.audiendo.query;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mysql.jdbc.ResultSet;

import it.graficheaquilane.audiendo.implementation.JdbcMysqlConnection;

public class T_Lavori_FDL {
	
	private String DATABASE;
	private JdbcMysqlConnection mysqlConnection;
	private PreparedStatement preparedStatement;
	private ResultSet resultSet;
	private List<String> result = new ArrayList<>();

	public T_Lavori_FDL(String database) {		
		this.DATABASE = database;		
		//Mi connetto al db per fare le query sulla tabella
		this.mysqlConnection = new JdbcMysqlConnection(DATABASE);		
	}
	
	public List<String> cercaFDL(String query){
		try {
			preparedStatement = mysqlConnection.getConnection().prepareStatement(query);
			//Eseguo la query
		    resultSet = (ResultSet) preparedStatement.executeQuery();
		    
		    //Resituisco tutti i lotti trovati
		    while (resultSet.next()) result.add(resultSet.getString("FDL")); 
		    		    
		} catch (SQLException e) {
			System.out.println("T_Lavori_FDL: Errore esecuzione query");
			chiudiConnessione();
			e.printStackTrace();
		}
	    
		return result;
	}
	
	public void setRistampaFDL(String FDL){
		
		Date today = new Date();
		SimpleDateFormat DATE_FORMAT_LAVORATI = new SimpleDateFormat("yyyy-MM-dd");
		String dataRistampa = DATE_FORMAT_LAVORATI.format(today);
		
		String query = "UPDATE t_Lavori_FDL SET Ristampa='1', DataRichiestaRistampa='" + dataRistampa + "', DataRistampa=NULL WHERE FDL='" + FDL + "';";
		//System.out.println("query update: " + query);
		try {
			preparedStatement = mysqlConnection.getConnection().prepareStatement(query);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			System.out.println("T_Lavori_FDL: Errore esecuzione update FDL ");
			chiudiConnessione();
			e.printStackTrace();
		}
	}

	public void chiudiConnessione(){
		mysqlConnection.closeConnection();		
	}
	
}
