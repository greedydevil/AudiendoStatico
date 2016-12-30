package it.graficheaquilane.audiendo.model;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import it.graficheaquilane.audiendo.implementation.JdbcMysqlConnection;

public class FDL {
	
	private String csvFile,database,line,cvsSplitBy = ",",lineaStampa = "";
	private String SELACC,LOTTO,MIXER,SOURCE1,SOTTO_LOTTO,DESCRIZIONE,BACINO,PORTO,SHEETS,ENVELOPES,SO_ENVELOPE_AM_COUNT,SO_ENVELOPE_CP_COUNT,SO_ENVELOPE_EU_COUNT,STAMPA,
	IMBUSTA,POSTA,PL,PSW1,PSWSL,STATO,DATA_CARICAMENTO,SUPPLEMENT,FOGLIO_L,PSW,BULLETIN,PERFORATE,POSTALIZZA,LP_CODICE,CODICE_UTENTE,
	OMO,WS_ID,WS_DATA_EMISSIONE,WS_STAMPATO,WS_IMBUSTATO,WS_SORTER,RAGSOC,DATA_ARRIVO,RAGSOC_GRUPPO,COLORE,ID_CLIENTE,ID_PRODOTTO_POSTALE,NOME_FILE;
	int contatoreLinee = 0;
	String[] result;
	
	public FDL(String pathFDL,String database){
		
		this.csvFile = pathFDL;
		this.database = database;
		
		//Mi connetto al db per salvare il CSV
		JdbcMysqlConnection mysql = new JdbcMysqlConnection(database);
		
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
		int contatoreLinee = 0;
		try {

			br = new BufferedReader(new FileReader(csvFile));
			
			while ((line = br.readLine()) != null) {
				
				contatoreLinee++;
			    
				if(contatoreLinee!=1){
			    	result = line.split(cvsSplitBy);
			    	
			    	SELACC = result[0]; LOTTO = result[1]; MIXER = result[2]; SOURCE1 = result[3]; SOTTO_LOTTO = result[4]; DESCRIZIONE = result[5]; BACINO = result[6];
			    	PORTO = result[7]; SHEETS = result[8]; ENVELOPES = result[9]; SO_ENVELOPE_AM_COUNT = result[10]; SO_ENVELOPE_CP_COUNT = result[11]; SO_ENVELOPE_EU_COUNT = result[12];
			    	STAMPA = result[13]; IMBUSTA = result[14]; POSTA = result[15]; PL = result[16]; PSW1 = result[17]; PSWSL = result[18]; STATO = result[19]; DATA_CARICAMENTO = result[20];
			    	SUPPLEMENT = result[21]; FOGLIO_L = result[22]; PSW = result[23]; BULLETIN = result[24]; PERFORATE = result[25]; POSTALIZZA = result[26]; 
			    	LP_CODICE = result[27]; CODICE_UTENTE = result[28]; OMO = result[29]; WS_ID = result[30];WS_DATA_EMISSIONE = result[31]; WS_STAMPATO = result[32]; 
			    	WS_IMBUSTATO = result[33]; WS_SORTER = result[34]; RAGSOC = result[35]; DATA_ARRIVO = result[36];RAGSOC_GRUPPO = result[37];COLORE = result[38]; 
			    	ID_CLIENTE = result[39];ID_PRODOTTO_POSTALE = result[40];NOME_FILE = result[41]; 
			    	
			    	lineaStampa += line + "\n";
			    	
			    	//Stampa a video quello letto
			    	/*
			    	System.out.println(contatoreLinee + ") [selacc= " + SELACC + ", lotto=" + LOTTO + ", MIXER=" + MIXER + ", SOURCE1=" + SOURCE1+ ", SOTTO_LOTTO=" + SOTTO_LOTTO 
							+ ", DESCRIZIONE=" + DESCRIZIONE + ", BACINO=" + BACINO + ", PORTO=" + PORTO + ", SHEETS=" + SHEETS + ", ENVELOPES=" + ENVELOPES + ", AM=" + SO_ENVELOPE_AM_COUNT 
							+ ", CP=" + SO_ENVELOPE_CP_COUNT + ", EU=" + SO_ENVELOPE_EU_COUNT + ", STAMPA=" + STAMPA + ", IMBUSTA=" + IMBUSTA + ", POSTA=" + POSTA + ",PL=" + PL  
							+ ", PSW1=" + PSW1 + ", PSWSL=" + PSWSL + ", STATO=" + STATO + ", DATA_CARICAMENTO=" + DATA_CARICAMENTO + ", SUPPLEMENT=" + SUPPLEMENT + ", FOGLIO_L=" + FOGLIO_L 
							+ ", PSW=" + PSW + ", BULLETIN=" + BULLETIN + ", PERFORATE=" + PERFORATE + ", POSTALIZZA=" + POSTALIZZA + ", LP_CODICE=" + LP_CODICE 
							+ ", CODICE_UTENTE=" + CODICE_UTENTE + ", OMO=" + OMO + ", WS_ID=" + WS_ID + ", WS_DATA_EMISSIONE=" + WS_DATA_EMISSIONE + ", WS_STAMPATO=" + WS_STAMPATO 
							+ ", WS_IMBUSTATO=" + WS_IMBUSTATO + ", WS_SORTER=" + WS_SORTER + ", RAGSOC=" + RAGSOC + ", DATA_ARRIVO=" + DATA_ARRIVO + ", RAGSOC_GRUPPO=" + RAGSOC_GRUPPO 
							+ ", COLORE=" + COLORE + ", ID_CLIENTE=" + ID_CLIENTE + ", ID_PRODOTTO_POSTALE=" + ID_PRODOTTO_POSTALE + ", NOME_FILE=" + NOME_FILE +
								"]");
			    	*/
			    	
			    	//Inserisco i dati nel DB
			    	
			    	String query = "INSERT INTO t_Lavori_Sottolotti (SELACC, LOTTO, MIXER, SOURCE1, SOTTO_LOTTO, Descrizione, BACINO, PORTO, SHEETS, ENVELOPES, so_envelope_am_count, "
			    			+ "so_envelope_cp_count, so_envelope_eu_count, STAMPA, IMBUSTA, POSTA, PL, PSW1, PSWSL, STATO, DATA_CARICAMENTO, Supplement, FOGLIO_L, PSW, Bulletin, "
			    			+ "Perforate, POSTALIZZA, LP_CODICE, CODICE_UTENTE, Omo, WS_ID, WS_DATA_EMISSIONE, WS_STAMPATO, WS_IMBUSTATO, WS_SORTER, RAGSOC, DATA_ARRIVO, RAGSOC_GRUPPO, "
			    			+ "COLORE, IDCliente, IDProdottoPostale, NomeFile)"
						    	 + " VALUES ('" + SELACC + "','" + LOTTO + "','" + MIXER + "','" + SOURCE1 + "','" + SOTTO_LOTTO + "','" + DESCRIZIONE + "','" + BACINO + "','" 
						    	 + PORTO + "','" + SHEETS + "','" + ENVELOPES + "','" + SO_ENVELOPE_AM_COUNT + "','" + SO_ENVELOPE_CP_COUNT + "','" + SO_ENVELOPE_EU_COUNT + "','" 
						    	 + STAMPA + "','" + IMBUSTA + "','" + POSTA + "','" + PL + "','" + PSW1 + "','" + PSWSL + "','" + STATO + "','" + DATA_CARICAMENTO + "','" 
						    	 + SUPPLEMENT + "','" + FOGLIO_L + "','" + PSW + "','" + BULLETIN + "','" + PERFORATE + "','" + POSTALIZZA + "','" + LP_CODICE + "','" 
						    	 + CODICE_UTENTE + "','" + OMO + "','" + WS_ID + "','" + WS_DATA_EMISSIONE + "','" + WS_STAMPATO + "','" + WS_IMBUSTATO + "','" + WS_SORTER + "','" 
						    	 + RAGSOC + "','" + DATA_ARRIVO + "','" + RAGSOC_GRUPPO + "','" + COLORE + "','" + ID_CLIENTE + "','" + ID_PRODOTTO_POSTALE + "','" + NOME_FILE + "');"; 
			    	
			    	//System.out.println("Ho eseguito la query: " + query);
			    	 
		    	    // create the mysql insert preparedstatement
		    	    PreparedStatement preparedStatement = mysql.getConnection().prepareStatement(query);
		    	    //preparedStatement.setString (1, "Barney");
		    	    //Eseguo la query
		    	    preparedStatement.execute();  		    	
			    	
			    }
			   
			}
			
			System.out.println("FDL caricato correttamente");
			
		} catch (FileNotFoundException e) {
			System.out.println("FDL - File non trovato");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("FDL - Errore buffered reader");
			e.printStackTrace();
		} catch (SQLException e) {
			System.out.println("FDL - Errore nell'SQL - Inserimento FDL fallito");
			e.printStackTrace();
		} finally {
			//Chiudo la connessione al db
			mysql.closeConnection();
			
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	
	}
	
	public String getCsvFile() {
		return csvFile;
	}

	public String getDatabase() {
		return database;
	}

	public String getLineaStampa() {
		return lineaStampa;
	}

	public String[] getResult() {
		return result;
	}

	public void stampaFDL(){		
		System.out.println("Stampa del FDL: ");
		System.out.println(lineaStampa);
	}
	

}
