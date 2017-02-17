package it.graficheaquilane.audiendo.model;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

public class ConnectionSFTP {
	
	private String host,username,password,directory;
	private int port;
	private Session session;
	private Channel channel;
	private ChannelSftp connection;
	private Vector<ChannelSftp.LsEntry> files;
	
	public ConnectionSFTP(String cliente) {
		
		cliente = cliente.toLowerCase();
		
		switch (cliente) {
				
			case "roma":
				
				this.host = "192.168.1.222";
				this.port = 22;
				this.username = "RomaCapitale";
				this.password = "Ro1maC7le17";				
				
				break;	
				
			default:
				break;
		}
		
		//Creo fisicamente una connessione FTP
		setConnection(cliente);				
	}
	
	private void setConnection(String cliente) {
		
		try{
			JSch jsch = new JSch();
			session = jsch.getSession(username, host, port);
			session.setConfig("StrictHostKeyChecking", "no");
	        session.setPassword(password);
	        session.connect();

	        channel = session.openChannel("sftp");
	        channel.connect();
	        connection = (ChannelSftp) channel;	       

		} catch (JSchException e) {
			System.out.println("setConnection - Connessione al server sftp fallita.");
	        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
	    } 
        		
	}
	
	public ChannelSftp getConnection() {
		return this.connection;
	}

	@SuppressWarnings("unchecked")
	public Vector<ChannelSftp.LsEntry> getInfoDirectory(String lavorazione) {
		
		lavorazione = lavorazione.toLowerCase();
		
		try{
			
			switch (lavorazione) {
				case "comune_roma":				
					directory = "/rootsftp";
					connection.cd(directory);
					break;
					
				default:
					directory = "/TEST";
					connection.cd(this.directory);
					break;
			
			}
			
			connection.cd("/rootsftp");
			
			files = connection.ls("*");
			
		} catch (SftpException e1) {
			System.out.println("Errore getInfoDirectory - Errore nel cambio directory nell'sftp o nella lista delle directory");
			e1.printStackTrace();
		}
		
		return files;
		
	}
	
	
	public String getCurrentDirectory(){
		return this.directory;
	}
	
	public void closeConnection(){
		connection.exit();
        session.disconnect();		
	}
	
	public String getFileSFTP(File temporaryDirectory,String nomeFile){
		 
		String temporaryLocalPath = temporaryDirectory.toString() + "/" + nomeFile;
		String temporaryServerPath = this.directory + "/" + nomeFile;
		        
		try {
        				
			File downloadFile = new File(temporaryLocalPath);
            BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(downloadFile));
            
            connection.get(temporaryServerPath, outputStream);
            
            outputStream.close();
			
		} catch (IOException e) {
			System.out.println("getFileFTP - Errore nell'accesso all'sftp");
			e.printStackTrace();
		} catch (SftpException e) {
			System.out.println("getFileSFTP - Errore nell'accesso al file");
			e.printStackTrace();
		}
	
		return temporaryLocalPath;
	}
	
	//copia un file dalla cartella locale in una cartella remota
	public void copyFileSFTP(String source,String destination){
		
		try {
        	InputStream Source = new FileInputStream(source);			
			connection.put(Source, destination);			
			Source.close();						
		} catch (IOException e) {
			System.out.println("moveFileSFTP - Errore nella creazione del file InputStream!");
			e.printStackTrace();
		} catch (SftpException e) {
			System.out.println("moveFileSFTP - Errore nello spostamento del file!");
			e.printStackTrace();
		}
		
	}

	public void createRemoteFolder(String path){
		
		try {
			connection.mkdir(path);			
		} catch (SftpException e) {
			System.out.println("createRemoteFolder - Errore nella creazione remota della cartella " + path);
			e.printStackTrace();
		}
		
	}
	
	public void deleteFileSFTP(String nomeFile){
		try {
			connection.rm(nomeFile);
		} catch (SftpException e) {
			System.out.println("deleteFileSFTP - Errore nell'eliminazione del file");
			e.printStackTrace();
		}
		
	}
	
	public boolean verificaDirectory(String path, String nomeDirectory){
		//Verifico se nel path esiste la directory "nomeDirectory"
		boolean trovato = false;
		
		try {
			String pathIniziale = connection.pwd();
			connection.cd(path); //Cambio il path e verifico se esiste nomeDirectory
			@SuppressWarnings("unchecked")
			Vector<ChannelSftp.LsEntry> result = connection.ls("*");
			
			for(ChannelSftp.LsEntry entry : result) {
				if((entry.getFilename()).equals(nomeDirectory)) trovato=true;				
			}
			
			connection.cd(pathIniziale);
		} catch (SftpException e) {
			System.out.println("verificaDirectory - Errore nella ricerca della directory");
			e.printStackTrace();
		} 		
		
		return trovato;
	}
}
