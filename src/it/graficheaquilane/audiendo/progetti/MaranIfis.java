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
import it.graficheaquilane.audiendo.model.ComandoPrintNet;
import it.graficheaquilane.audiendo.model.ConnectionFTP;
import it.graficheaquilane.audiendo.model.Email;
import it.graficheaquilane.audiendo.model.FDL;
import it.graficheaquilane.audiendo.model.TemporaryDirectory;
import it.graficheaquilane.audiendo.model.UnzipUtility;

public class MaranIfis {

	@SuppressWarnings("deprecation")
	public MaranIfis(int debug) throws IOException{
		
		if(debug!=0){
			
			System.out.println("");
			System.out.println("Inizio lavorazione Maran Ifis");
			
			/****************** COSTANTI - CAMBIARE IN CASO DI CAMBIO INDIRIZZO ******************/
			String DATABASE = "graficheaquilane"; // graficheaquilane
			String CLIENT_FTP = "Maran";
			String DIR_CLIENT_FTP;
			String PATH_SCARICATI = "\\\\192.168.1.10\\Scaricati\\";
			//variabili ComandoPrintnet
			String NOME_LAVORAZIONE = "Programmi\\MARAN";
			String TIPO_LAVORAZIONE = "Ifis";			
			String CARTELLA_DATI_INPUT = "";
			String NOME_FILE_INPUT = "";
			String TIPO_FORMATO_OUTPUT = "PDF";
			String NOME_MODULO_OUTPUT = "AFP"; 
			Boolean SPLIT = true;
			String CARTELLA_DATI_OUTPUT = "";
			String NOME_FILE_OUTPUT = "%h[3].%e";
			
			/*************************************************************************************/
			
			//Cerco l'ultimo progetto presente su db
			ComandoPrintNet maran_ifis = new ComandoPrintNet(NOME_LAVORAZIONE, TIPO_LAVORAZIONE);
			String downloadLocale = maran_ifis.getPathProgetto().toString();
			String destinazioneLocale = downloadLocale + "\\Dati\\";
			
			//Prima di scaricarci nuovi file pulisco le directory da eventuali file temporanei precedenti
			TemporaryDirectory downloadDati = new TemporaryDirectory(destinazioneLocale);
			downloadDati.svuotaDirecdtory(downloadDati.getDirectoryName());
			
			TemporaryDirectory serviceDownloadDirectory = new TemporaryDirectory(destinazioneLocale + "\\download_tmp");
			TemporaryDirectory servicePdfDirectory = new TemporaryDirectory(destinazioneLocale + "\\pdf_tmp");
			TemporaryDirectory serviceDirStampa = new TemporaryDirectory(destinazioneLocale + "\\tmp");
			
			CARTELLA_DATI_OUTPUT = serviceDirStampa.getDirectoryName().toString();
						
			/*
			//Rimuovo vecchie cartelle non ancora cancellate prima di creare le nuove
			serviceDownloadDirectory.removeTemporaryDir();
			servicePdfDirectory.removeTemporaryDir();
			serviceDirStampa.removeTemporaryDir();
			*/
			serviceDownloadDirectory.createTemporaryDir();
			servicePdfDirectory.createTemporaryDir();
			serviceDirStampa.createTemporaryDir();
			
			
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
			ConnectionFTP ftp = new ConnectionFTP(CLIENT_FTP);
			
			//Mi faccio dare la lista dei file e delle directory - se sono in modalità debug carico la cartella test
			if(debug==1)
				DIR_CLIENT_FTP = "";
			else
				DIR_CLIENT_FTP = "ifis";
			
			result = ftp.getInfoDirectory(DIR_CLIENT_FTP); 
			
			ftp.createRemoteFolder(ftp.getCurrentDirectory() + "/Lavorati/" + cartallaLavorati); //Creo una cartella remota dove sposterò i file lavorati
					
			//Ogni file zip lo estraggo
			for (FTPFile file : result) {
						    
				nomeFile = file.getName();
				
			    //Se il file contiene l'estensione .zip lo scarico ed estraggo in una cartella temporanea
			    if (!file.isDirectory() && (nomeFile.contains(".zip") || nomeFile.contains(".ZIP"))) {
			    	//Incremento il contatore dei file scaricati dall'ftp
			    	contatoreFileZip++;
			    	
			    	//scarico il file trovato in una cartella temporanea 
			    	pathLocalTmp = ftp.getFileFTP(serviceDownloadDirectory.getDirectoryName(),nomeFile); //Gli passo il nome della cartella temporanea ed il nome del file
			    	
			    	//Se non sono in debug mode sposto i file anche nella cartella FTP
			    	if(debug!=1){
						ftp.copyFileFTP(serviceDownloadDirectory.getDirectoryName() + "/" + nomeFile, ftp.getCurrentDirectory() + "/Lavorati/" + cartallaLavorati + "/" + nomeFile); //Copio i file dalla cartella locale nella cartella remota (lavorati/Data)
						ftp.deleteFileFTP(ftp.getCurrentDirectory() + "/" + nomeFile); //Cancello il file dalla cartella principale
			    	}
			    	
			    	//Estraggo il file in una cartella (la cartella si chiamerà come il file)		    	
			    	new UnzipUtility(pathLocalTmp, (pathLocalTmp.replace(".zip", "")));
			    		    	
			    	//Cerco il nome del file all'interno della cartella estratta (del singolo utente)
			    	File temporaryDir = new File(pathLocalTmp.replace(".zip", "")); //Creo un oggetto Directory con il nome del file senza estensione (es: download_tmp/123456_Cesidio Aureli/
			    	directorySingleUser = temporaryDir.list(); 		    	
			    	
			    	//Conto le pagine dei singoli pdf
			    	int lettera =0,proposta = 0,informativa = 0;	
			    	
			    	//creo un array di stringhe e lo riempio con la lista dei files presenti nella directory
			    	for (int i=0;i<directorySingleUser.length;i++) {
			    		
			    		//Mi prendo il prefisso del nome del file per salvarlo nell'unione finale (es: 123456_cesidio aureli_Informativa.pdf lo salverò come 123456_cesidio aureli.pdf)
			    		resultSplitApp = directorySingleUser[i].split("_");
			    		if (i==0) nomeFilePdfFinale = resultSplitApp[1] + "_" + resultSplitApp[0] + ".pdf"; //Mi salvo solo l'id dell'utente ed il nome
			    
			    		if(StringUtils.containsIgnoreCase(resultSplitApp[2], "lettera")) {
			    			pdf1 = new File(temporaryDir.toString() + "/" + directorySingleUser[i]);
			    			PdfReader reader1 = new PdfReader(temporaryDir.toString() + "/" + directorySingleUser[i]);
			    			lettera = reader1.getNumberOfPages();
			    		}
			    		else if(StringUtils.containsIgnoreCase(resultSplitApp[2], "proposta")) {
			    			pdf2 = new File(temporaryDir.toString() + "/" + directorySingleUser[i]);
			    			PdfReader reader2 = new PdfReader(temporaryDir.toString() + "/" + directorySingleUser[i]);
			    			proposta = reader2.getNumberOfPages();
			    		}
			    		else if(StringUtils.containsIgnoreCase(resultSplitApp[2], "informativa")) {
			    			pdf3 = new File(temporaryDir.toString() + "/" + directorySingleUser[i]);
			    			PdfReader reader3 = new PdfReader(temporaryDir.toString() + "/" + directorySingleUser[i]);
			    			informativa = reader3.getNumberOfPages();
			    		}
			    		else throw new IllegalArgumentException("Il file in esame (" + nomeFilePdfFinale + ") non contiene nessuna delle tre parole cercate: 1)Lettera 2)Proposta 3)Informativa");
			    				    		
			    		//System.out.println(directorySingleUser[i]);
			    	}
			    	
			    	//Se la lettera o la proposta contiene più di una pagina e se l'informativa non ha 4 pagine blocco l'esecuzione
			    	if(lettera!=1 || proposta!=1) throw new IllegalArgumentException("La lettera o la proposta del file " + nomeFilePdfFinale  + " contiene più di una pagina");
			    	if(informativa!=4) throw new IllegalArgumentException("L'informativa del file " + nomeFilePdfFinale  + " non contiene esattamente 4 pagine");
			    	
			    	//Faccio il merge dei tre pdf secondo l'ordine stabilito da maran (1° Lettera, 2° Proposta, 3° Informativa)
			    	PDFMergerUtility merger = new PDFMergerUtility();
			    	
			    	merger.addSource(pdf1);
			    	merger.addSource(pdf2);
			    	merger.addSource(pdf3);
			    	merger.setDestinationFileName(servicePdfDirectory.getDirectoryName() + "/" + nomeFilePdfFinale);
			    	merger.mergeDocuments();
	
			    	//Salvo il file elaborato nell'array risultato
					arrayResult.add(nomeFile);	
					temporaryDir.delete();
			
			    }
			}
			
			if(contatoreFileZip!=0){ 
				
				//Una volta estratti tutti i file faccio il merge finale dei singoli file (ogni file - utente - avrà 6 pagine)
				PDFMergerUtility merger = new PDFMergerUtility();
				directoryPDF = servicePdfDirectory.getDirectoryName().list();
				
				for (int i=0;i<directoryPDF.length;i++) {
					merger.addSource(servicePdfDirectory.getDirectoryName() + "/" + directoryPDF[i]);
					contatorePDF++;
				}
						
				//nomeFilePdfFinale = date + "_MARAN_IFIS.pdf";
				nomeFilePdfFinale = date + ".pdf";
				
				merger.setDestinationFileName(servicePdfDirectory.getDirectoryName() + "/" + nomeFilePdfFinale);
		    	merger.mergeDocuments();
				
		    	//Copio il file generato sull'8 e rimuovo la cartella temporanea
		    	destinazioneLocale += nomeFilePdfFinale;
		    	servicePdfDirectory.copyFile(servicePdfDirectory.getDirectoryName()+ "/" + nomeFilePdfFinale, destinazioneLocale);
		    	servicePdfDirectory.removeTemporaryDir();
		    	//Cancello la cartella "download_tmp" 
		    	serviceDownloadDirectory.removeTemporaryDir();
		    	 
		    	//Chiudo la connessione all'FTP
				ftp.closeConnection();
				
				System.out.println("Ho scaricato dall'ftp un totale di " + contatoreFileZip + " file zip");
				System.out.println("Ho mergiato un totale di " + contatorePDF + " pdf");
				
				//Lancio PrintNet
				maran_ifis = new ComandoPrintNet(NOME_LAVORAZIONE, TIPO_LAVORAZIONE, CARTELLA_DATI_INPUT, NOME_FILE_INPUT, TIPO_FORMATO_OUTPUT, NOME_MODULO_OUTPUT, SPLIT, CARTELLA_DATI_OUTPUT, NOME_FILE_OUTPUT);				
				ComandoDOS servicePnet = new ComandoDOS(maran_ifis.getComando());
				Boolean resultExec = servicePnet.getResult();
				
				//Se PrintNet non và in errore sposto i file generati su una cartella del 192.168.1.10 e cancello il file generato nella cartella locale 
				if(resultExec && debug!=1) {
					
					String[] resultDir = serviceDirStampa.getListFileDir();
					for (String file : resultDir) {
						String lotto = (file.split("_")[0]);
						String destination = PATH_SCARICATI + lotto;
						TemporaryDirectory serviceDirDestination = new TemporaryDirectory(destination);
						serviceDirDestination.createTemporaryDir();
						destination += "\\" + file; 
						String source = serviceDirStampa.getDirectoryName().toString() + "\\" + file;
						
						if(StringUtils.containsIgnoreCase(file, "FDL") && StringUtils.containsIgnoreCase(file, ".csv")) 
							//Carico il csv nell'intranet
							new FDL(source,DATABASE);
					
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
					Email e = new Email("ced@nuovaipa.com", "s.bartoloni@call.maran.it", "ced@nuovaipa.com;j.rosati@call.maran.it;f.brunetti@call.maran.it","Nuovo file elaborato da NuovaIpa", corpoEmail);
					e.sendMail();
							
				}
				
				//Rimuovo anche il file temporaneo (quello che usa PrintNet per generare quello con le tacche! e la cartella download
				(new File(destinazioneLocale)).delete();
				serviceDownloadDirectory.removeTemporaryDir();
				
				if(debug==1) {
					//Se sono in debug mode non sposto i file e non creo cartelle temporanee quindi elimino i file d'appoggio che genero
					//TemporaryDirectory tmpFolder = new TemporaryDirectory("C:\\AFP\\MARAN_IFIS\\tmp");
					String[] resultTMPfolder = serviceDirStampa.getListFileDir();
					for (String file : resultTMPfolder) {
						String sourceTMP = serviceDirStampa.getDirectoryName().toString() + "\\" + file;
						(new File(sourceTMP)).delete();						
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
					Email e = new Email("ced@nuovaipa.com", "cesidio.aureli@nuovaipa.com", "Nuovo file elaborato da NuovaIpa", corpoEmail);
					e.sendMail();
				}
				
			} // Chiudo if(contatoreFileZip!=0)
			else {
				//Rimuovo le cartelle temporanee
				serviceDownloadDirectory.removeTemporaryDir();
				servicePdfDirectory.removeTemporaryDir();
				serviceDirStampa.removeTemporaryDir();
				System.out.println("Non ci sono file da elaborare sull'FTP");
			}
			
			//Rimuovo la cartella temporanea di download e tutte le sottocartelle
			//new ComandoDOS("removeDownloadTMP.bat");
	
			System.out.println("Lavorazione terminata");
			
		} else System.out.println("Maran_Ifis: lavorazione esclusa dall'esecuzione");
		
	}

}
