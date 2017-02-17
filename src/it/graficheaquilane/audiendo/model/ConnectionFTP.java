package it.graficheaquilane.audiendo.model;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.ftp.FTPSClient;

public class ConnectionFTP {
	
	private String host,username,password,directory;
	private int port;
	private FTPClient connection;
	private FTPFile[] files;
	
	public ConnectionFTP(String cliente) {
		
		cliente = cliente.toLowerCase();
		
		switch (cliente) {
		
			case "maran":
				
				this.host = "192.168.1.222";
				this.port = 21;
				this.username = "maran";
				this.password = "maran!2013";
						    		    
				break;
				
			case "metamer":
				break;
				
			
			case "roma":
				
				this.host = "192.168.1.222";
				this.port = 21;
				this.username = "cesidio";
				this.password = "cesidio";
								
				break;	
				
			default:
				break;
		}
		
		//Creo fisicamente una connessione FTP
		setConnection(cliente);				
	}

	public FTPFile[] getInfoDirectory(String lavorazione) {
		
		lavorazione = lavorazione.toLowerCase();
		
		switch (lavorazione) {
		
			case "ifis":				
				this.directory = "PDF_IN";								    		    
				break;
				
			case "ss_unificato":
				this.directory = "PROD_IN/SS5";
				break;
				
			case "test_unificato":
				this.directory = "TEST/PROD_IN/SS5";
				break;
				
			case "comune_roma":
				this.directory = "ROOTSFTP/";
				break;
			
			default:
				this.directory = "TEST";
				break;
		}

		try {
			files = connection.listFiles(directory);
									
		} catch (IOException e) {
			System.out.println("getInfoDirectory - Errore listing directory");
			e.printStackTrace();
		}
		return files;
		
	}

	private void setConnection(String cliente) {
		
		this.connection = new FTPClient();
				
		try {
			connection.connect(host, port);
            int replyCode = connection.getReplyCode();
            if (!FTPReply.isPositiveCompletion(replyCode)) {
                System.out.println("setConnection - Connessione al server ftp " + cliente + " fallita. Il server ha restituito il seguente errore: " + replyCode);
                return;
            }
            boolean success = connection.login(username, password);
         
            if (!success) {
                System.out.println("setConnection - Errore login ftp");
                return;
            } 
        } catch (IOException ex) {
            System.out.println("setConnection - Oops! Something wrong happened");
            ex.printStackTrace();
        }
		
	}
	
	public String getCurrentDirectory(){
		return this.directory;
	}
	
	public void closeConnection(){
		try {
			this.connection.logout();
			this.connection.disconnect();
		} catch (IOException e) {
			System.out.println("closeConnection - Errore durante la disconnessione dell'FTP");
			e.printStackTrace();
		}
		
	}
	
	public String getFileFTP(File temporaryDirectory,String nomeFile){
		 
		String temporaryLocalPath = temporaryDirectory.toString() + "/" + nomeFile;
		String temporaryServerPath = this.directory + "/" + nomeFile;
		//System.out.println("sto scaricando il file: " + temporaryLocalPath);
        
		try {
        	connection.enterLocalPassiveMode();
			connection.setFileType(FTP.BINARY_FILE_TYPE);
			
			File downloadFile = new File(temporaryLocalPath);
            BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(downloadFile));
             
            if (!connection.retrieveFile(temporaryServerPath, outputStream)) 
            	System.out.println("getFileFTP - Errore download file");
             
            outputStream.close();
			
		} catch (IOException e) {
			System.out.println("getFileFTP - Errore nell'accesso in passive mode");
			e.printStackTrace();
		}
	
		return temporaryLocalPath;
	}
	
	//copia un file dalla cartella locale in una cartella remota
	public void copyFileFTP(String source,String destination){
		
		try {
        	connection.enterLocalPassiveMode();
			connection.setFileType(FTP.BINARY_FILE_TYPE);
			InputStream Source = new FileInputStream(source);
			if(!connection.storeFile(destination, Source))
				System.out.println("moveFileFTP - Errore nello spostamento del file in " + destination);
			Source.close();
						
		} catch (IOException e) {
			System.out.println("moveFileFTP - Qualcosa è andato storto!");
			e.printStackTrace();
		}
		
	}

	public void createRemoteFolder(String path){
		
		try {
			connection.makeDirectory(path);
		} catch (IOException e) {
			System.out.println("createRemoteFolder - Errore nella creazione remota della cartella " + path);
			e.printStackTrace();
		}
		
	}
	
	public void deleteFileFTP(String nomeFile){
		try {
			connection.deleteFile(nomeFile);
		} catch (IOException e) {
			System.out.println("deleteFileFTP - Errore nell'eliminazione del file");
			e.printStackTrace();
		}
		
	}
}
