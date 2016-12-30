package it.graficheaquilane.audiendo.progetti;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.pdfbox.multipdf.PDFMergerUtility;

import com.itextpdf.text.pdf.PdfReader;

import it.graficheaquilane.audiendo.model.ComandoDOS;
import it.graficheaquilane.audiendo.model.ConnectionFTP;
import it.graficheaquilane.audiendo.model.Email;
import it.graficheaquilane.audiendo.model.FDL;
import it.graficheaquilane.audiendo.model.TemporaryDirectory;
import it.graficheaquilane.audiendo.model.UnzipUtility;

public class MaranSSunificato {

	@SuppressWarnings("deprecation")
	public MaranSSunificato(int debug) throws IOException{
		
		if(debug!=0){
	
			String nomeFile,pathLocalTmp,nomeFilePdfFinale = null;
			FTPFile[] result;
			String directorySingleUser[],resultSplitApp[],directoryPDF[];
			File pdf1 = null, pdf2 = null, pdf3 = null;
			int contatorePDF = 0,contatoreFileZip = 0;
			ArrayList<String> arrayResult = new ArrayList<>();
			
			//Mi prendo la data e l'ora odierna
			Date today = new Date();
			SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMddHHmm");
			SimpleDateFormat DATE_FORMAT_LAVORATI = new SimpleDateFormat("yyyyMMdd");
			String date = DATE_FORMAT.format(today);
			String cartallaLavorati = DATE_FORMAT_LAVORATI.format(today);
			
			
			//Creo la connessione all'FTP
			ConnectionFTP ftp = new ConnectionFTP("Maran");
			
			//Mi faccio dare la lista dei file e delle directory - se sono in modalità debug carico la cartella test
			if(debug==1)
				result = ftp.getInfoDirectory("test_unificato");
			else
				result = ftp.getInfoDirectory("ss_unificato"); 
			
			//Creo una cartella temporanea per il download
			TemporaryDirectory serviceDownloadDirectory = new TemporaryDirectory("tmp_ss_unificato");
			//Rimuovo vecchie cartelle non ancora cancellate prima di crearne una nuova
			serviceDownloadDirectory.removeTemporaryDir();
			serviceDownloadDirectory.createTemporaryDir();
					
			//Ogni file zip lo estraggo
			for (FTPFile file : result) {
						    
				nomeFile = file.getName();
				
			    //Se il file contiene l'estensione .zip lo scarico ed estraggo in una cartella temporanea
			    if (!file.isDirectory() && (StringUtils.containsIgnoreCase(nomeFile, ".zip") || StringUtils.containsIgnoreCase(nomeFile, ".7z") )) {
			    	//Incremento il contatore dei file scaricati dall'ftp
			    	contatoreFileZip++;
			    	
			    	//scarico il file trovato in una cartella temporanea 
			    	pathLocalTmp = ftp.getFileFTP(serviceDownloadDirectory.getDirectoryName(),nomeFile); //Gli passo il nome della cartella temporanea ed il nome del file
			    	
			    	//Se non sono in debug mode sposto i file anche nella cartella FTP
			    	if(debug!=1){
						ftp.copyFileFTP(serviceDownloadDirectory.getDirectoryName() + "/" + nomeFile, ftp.getCurrentDirectory() + "/Lavorati/" + nomeFile); //Copio i file dalla cartella locale nella cartella remota (Lavorati/)
						ftp.deleteFileFTP(ftp.getCurrentDirectory() + "/" + nomeFile); //Cancello il file dalla cartella principale
			    	}
			    	
			    	//Estraggo il file in una cartella (la cartella si chiamerà come il file)		    	
			    	new UnzipUtility(pathLocalTmp, (pathLocalTmp.replace(".zip", "")));
			   		    	
			    	//Cerco il nome del file all'interno della cartella estratta (del singolo utente)
			    	File temporaryDir = new File(pathLocalTmp.replace(".zip", "")); //Creo un oggetto Directory con il nome del file senza estensione (es: download_tmp/123456_Cesidio Aureli/
			    	directorySingleUser = temporaryDir.list(); 		    	
			    				    	
			    	//creo un array di stringhe e lo riempio con la lista dei files presenti nella directory
			    	for (int i=0;i<directorySingleUser.length;i++) {
			    		
			    		//Mi prendo il prefisso del nome del file per salvarlo nell'unione finale (es: 123456_cesidio aureli_Informativa.pdf lo salverò come 123456_cesidio aureli.pdf)
			    		String nomeFileEstratto = directorySingleUser[i];
			    		System.out.println("sto leggendo il file: " + nomeFileEstratto);
			    				
			    		//System.out.println(directorySingleUser[i]);
			    	}
			    		
			    	//Salvo il file elaborato nell'array risultato
					arrayResult.add(nomeFile);
					temporaryDir.delete();
		
			    }
			}
		/*
			if(contatoreFileZip!=0){
										    	 
		    	//Chiudo la connessione all'FTP
				ftp.closeConnection();
				
				System.out.println("Ho scaricato dall'ftp un totale di " + contatoreFileZip + " file zip");
								
				//Lancio PrintNet
				ComandoDOS servicePnet = new ComandoDOS("MARAN_IFIS_PNET.bat");
				Boolean resultExec = servicePnet.getResult();
				
				//Se PrintNet non và in errore sposto i file generati su una cartella del 192.168.1.10 e cancello il file generato nella cartella locale 
				if(resultExec && debug!=1) {
					TemporaryDirectory serviceDirStampa = new TemporaryDirectory("C:\\AFP\\MARAN_IFIS\\tmp");
					String[] resultDir = serviceDirStampa.getListFileDir();
					for (String file : resultDir) {
						String lotto = (file.split("_")[0]);
						String destination = "\\\\192.168.1.10\\Scaricati\\" + lotto;
						TemporaryDirectory serviceDirDestination = new TemporaryDirectory(destination);
						serviceDirDestination.createTemporaryDir();
						destination += "\\" + file; 
						String source = serviceDirStampa.getDirectoryName().toString() + "\\" + file;
						
						if(StringUtils.containsIgnoreCase(file, "FDL") && StringUtils.containsIgnoreCase(file, ".csv")) 
							//Carico il csv nell'intranet
							new FDL(source,"graficheaquilane");
					
						//Sposto i file
						serviceDirStampa.moveFile(source, destination);
											
						System.out.println("Ho spostato i file nella cartella del 10: " + destination);			
					} 
						
					//Mando un'email al cliente con i resoconti dei file elaborati			
					String corpoEmail = "Salve,\n";
					corpoEmail += "sono presenti nuove lavorazioni Maran Ifis nello spazio FTP.\n";
					corpoEmail += "Di seguito i nomi dei file elaborati: \n\n";
					
					for(String nominativi : arrayResult){
						corpoEmail += " - " + nominativi + "\n";			
					}
					corpoEmail += "\n";			
					corpoEmail += "La lavorazione è stata presa in carico ed elaborata correttamente. \n\n";
					corpoEmail += "Buona giornata";
				
					//Email e = new Email("ced@nuovaipa.com", "cesidio.aureli@nuovaipa.com", "Nuovo file elaborato da NuovaIpa", corpoEmail);
					Email e = new Email("ced@nuovaipa.com", "s.bartoloni@call.maran.it", "ced@nuovaipa.com","Nuovo file elaborato da NuovaIpa", corpoEmail);
					e.sendMail();
							
				}
				
				//Rimuovo anche il file temporaneo (quello che usa PrintNet per generare quello con le tacche! e la cartella download
				(new File(destinazioneLocale)).delete();
				serviceDownloadDirectory.removeTemporaryDir();
				
				if(debug==1) {
					//Se sono in debug mode non sposto i file e non creo cartelle temporanee quindi elimino i file d'appoggio che genero
					TemporaryDirectory tmpFolder = new TemporaryDirectory("C:\\AFP\\MARAN_IFIS\\tmp");
					String[] resultTMPfolder = tmpFolder.getListFileDir();
					for (String file : resultTMPfolder) {
						String sourceTMP = tmpFolder.getDirectoryName().toString() + "\\" + file;
						(new File(sourceTMP)).delete();
						
					}				
				}
				
			} // Chiudo if(contatoreFileZip!=0)
			else {
				//Rimuovo le cartelle temporanee
				serviceDownloadDirectory.removeTemporaryDir();
				servicePdfDirectory.removeTemporaryDir();
				System.out.println("Non ci sono file da elaborare sull'FTP");
			}
			*/
			//Rimuovo la cartella temporanea di download e tutte le sottocartelle
			//new ComandoDOS("removeDownloadTMP.bat");
			serviceDownloadDirectory.removeTemporaryDir();
			System.out.println("Lavorazione terminata");
			
		} else System.out.println("Maran_SS_unificato: lavorazione esclusa dall'esecuzione");
	}// Chiudo il costruttore

}
