package it.graficheaquilane.audiendo.test;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;

import com.jcraft.jsch.ChannelSftp;

import it.graficheaquilane.audiendo.model.ConnectionSFTP;
import it.graficheaquilane.audiendo.model.TemporaryDirectory;
import it.graficheaquilane.audiendo.model.UnzipUtility;

public class TestSFTP {

	public static void main(String[] args) {
		
		String nomeFile,pathLocalTmp,nomeFilePdfFinale = null;
		Vector<ChannelSftp.LsEntry> result;
		String directorySingleUser[],resultSplitApp[],directoryPDF[];
		int contatorePDF = 0,contatoreFileZip = 0;
		ArrayList<String> arrayResult = new ArrayList<>();
		
		//Mi prendo la data e l'ora odierna
		Date today = new Date();
		SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMddHHmm");
		SimpleDateFormat DATE_FORMAT_LAVORATI = new SimpleDateFormat("yyyyMMdd");
		String date = DATE_FORMAT.format(today);
		String cartallaLavorati = DATE_FORMAT_LAVORATI.format(today);
		
		
		//Creo la connessione all'FTP
		ConnectionSFTP ftp = new ConnectionSFTP("roma");
		
		//Mi faccio dare la lista dei file e delle directory - se sono in modalità debug carico la cartella test
		result = ftp.getInfoDirectory("comune_roma");
					
		//Creo una cartella temporanea per il download e per i pdf
		TemporaryDirectory serviceDownloadDirectory = new TemporaryDirectory("download_tmp");
		TemporaryDirectory servicePdfDirectory = new TemporaryDirectory("pdf_tmp");
		
		//Rimuovo vecchie cartelle non ancora cancellate prima di crearne una nuova
		serviceDownloadDirectory.removeTemporaryDir();
		servicePdfDirectory.removeTemporaryDir();
		
		//Ricreo fisicamente le cartelle - così ho la certezza che sono vuote ad ogni accesso!
		serviceDownloadDirectory.createTemporaryDir();
		servicePdfDirectory.createTemporaryDir();
		
		if(!ftp.verificaDirectory(ftp.getCurrentDirectory() + "/Lavorati", cartallaLavorati)) 
				ftp.createRemoteFolder(ftp.getCurrentDirectory() + "/Lavorati/" + cartallaLavorati); //Creo una cartella remota dove sposterò i file lavorati	
		
		//Ogni file zip lo estraggo
		for(ChannelSftp.LsEntry entry : result) {
		    System.out.println("nome file: " + entry.getFilename()); 
		     
			nomeFile = entry.getFilename();
			//Se il file contiene l'estensione .pdz lo scarico ed estraggo in una cartella temporanea
			//Questi tipi di file contengono i pdf e non gli xml  
		    if ((nomeFile.contains(".pdz") || nomeFile.contains(".PDZ"))) {
		    	//Incremento il contatore dei file scaricati dall'ftp
		    	contatoreFileZip++;
		    	
		    	//scarico il file trovato in una cartella temporanea 
		    	pathLocalTmp = ftp.getFileSFTP(serviceDownloadDirectory.getDirectoryName(),nomeFile); //Gli passo il nome della cartella temporanea ed il nome del file
		    		
		    	String pathDestinazioneEstrazione = servicePdfDirectory.getDirectoryName()+ "/" + nomeFile.replace(".pdz", "");
		    	//Estraggo il file in una cartella (la cartella si chiamerà come il file)		    	
		    	new UnzipUtility(pathLocalTmp, pathLocalTmp.replace(".pdz", ""));
		    	
		    	//Se non sono in debug mode sposto i file anche nella cartella FTP
		    	ftp.copyFileSFTP(serviceDownloadDirectory.getDirectoryName() + "/" + nomeFile, ftp.getCurrentDirectory() + "/Lavorati/" + cartallaLavorati + "/" + nomeFile); //Copio i file dalla cartella locale nella cartella remota (lavorati/Data)
		    	ftp.deleteFileSFTP(ftp.getCurrentDirectory() + "/" + nomeFile); //Cancello il file dalla cartella principale
		    		    	
		    	//Cerco il nome del file all'interno della cartella estratta (del singolo utente)
		 		File temporaryDir = new File(pathLocalTmp.replace(".pdz", "")); //Creo un oggetto Directory con il nome del file senza estensione (es: download_tmp/123456_Cesidio Aureli/
		    	directorySingleUser = temporaryDir.list(); 		    	
		    	
		    	
		    	//System.out.println("Nome Cartella: " + temporaryDir.getName()); //Nome Directory
		    	//System.out.println("Nome Cartella: " + temporaryDir.getAbsolutePath()); //Path assoluto della cartella temporanea
		    	
		    	String[] children = temporaryDir.list();
		    	String nomeFileInput = "";
				
		    	//Scorro la cartella temporanea per cercarmi il file indice
				for(String nomeFileApp : children){
					if(nomeFileApp.contains(".bol")) 
						nomeFileInput = nomeFileApp;
						//System.out.println("Nome file input: " + nomeFileApp);
				}
		    	
		    	if(nomeFileInput=="") System.err.println("ComuneRoma - Errore nell'individuazione del file indice nella cartella temporanea");
		    	
		    	//Creo la cartella temporanea dove estrarre i file
		    	TemporaryDirectory pathTemporaneo = new TemporaryDirectory(temporaryDir.getAbsolutePath() + "\\TMP");
		    	
						
		    	
		    	
		    	//temporaryDir.delete();
		    	
		    	
		    } //fine file .pdz	
		}
		//Rimuovo la cartella temporanea download_tmp e pdf_tmp
		serviceDownloadDirectory.removeTemporaryDir();
		servicePdfDirectory.removeTemporaryDir();
		
		ftp.closeConnection();
		    
	}

}
