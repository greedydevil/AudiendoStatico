package it.graficheaquilane.audiendo.implementation;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.pdfbox.multipdf.PDFMergerUtility;

import it.graficheaquilane.audiendo.query.T_Lavori_Sottolotti;

//Prende i file dei lotti caricati sulla t_sottolotti (stato=1) e crea un file unico per la stampa
public class CopiaFileMaran {

	@SuppressWarnings("deprecation")
	public CopiaFileMaran(int debug) throws IOException{
						
		if(debug!=0){
			
			/****************** VARIABILI COSTANTI **********************************************/
			String PATH_FILE_OUTPUT = "\\\\192.168.1.8\\afp\\";
			String PATH_SCARICATI = "\\\\192.168.1.10\\Scaricati\\";
			String DATABASE = "graficheaquilane";
			/*************************************************************************************/
			
			System.out.println("");
			System.out.println("Inizio lavorazione Copia file Maran");
			
			String lotto,nomeFilePdfFinale = null;
			PDFMergerUtility merger = new PDFMergerUtility();
			int contatorePDF = 0;
			
			//Mi prendo la data e l'ora odierna
			Date today = new Date();
			SimpleDateFormat DATE_FORMAT_LAVORATI = new SimpleDateFormat("yyyyMMdd");
			String date = DATE_FORMAT_LAVORATI.format(today);
			
			nomeFilePdfFinale = date + "_MARAN_MERGE_SOTTOLOTTI.pdf"; 
			T_Lavori_Sottolotti sottolotti = new T_Lavori_Sottolotti(DATABASE);
			List<String> result = sottolotti.cercaLottiStampa("SELECT * FROM t_Lavori_Sottolotti WHERE (SELACC LIKE '%UNIFICATO%' OR SELACC LIKE '%IFIS%') AND IdStatoLavorazione='1' GROUP BY LOTTO");
			
			//Per ogni lotto trovato mi copio il file nella cartella temporanea
			for(int i=0;i<result.size();i++){
			    lotto = result.get(i);
			    String source = PATH_SCARICATI + lotto + "\\" + lotto + ".pdf";
			    contatorePDF++;
			    merger.addSource(source);
			} 
			
			if(contatorePDF>0){
				merger.setDestinationFileName(PATH_FILE_OUTPUT + nomeFilePdfFinale);
				merger.mergeDocuments();
				System.out.println("Ho mergiato " + contatorePDF + " file.");
				
			} else {
				System.out.println("Non ci sono file Maran da stampare");
			}
			
			System.out.println("Copia File Maran: Lavorazione terminata");
			
		} else System.out.println("Copia File Maran: lavorazione esclusa dall'esecuzione");
	}// Chiudo il costruttore

}
