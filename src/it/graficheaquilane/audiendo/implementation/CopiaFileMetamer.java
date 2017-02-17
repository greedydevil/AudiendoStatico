package it.graficheaquilane.audiendo.implementation;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.pdfbox.multipdf.PDFMergerUtility;

import it.graficheaquilane.audiendo.model.ComandoDOS;
import it.graficheaquilane.audiendo.model.ComandoPrintNet;
import it.graficheaquilane.audiendo.model.TemporaryDirectory;
import it.graficheaquilane.audiendo.query.T_Lavori_Sottolotti;

//Prende i file dei lotti caricati sulla t_sottolotti (stato=1) e crea un file unico per la stampa
public class CopiaFileMetamer {

	@SuppressWarnings("deprecation")
	public CopiaFileMetamer(int debug) throws IOException{
						
		if(debug!=0){
			
			System.out.println("");
			System.out.println("Inizio lavorazione Copia file Metamer");
			
			/****************** VARIABILI COSTANTI **********************************************/
			String DATABASE = "graficheaquilane"; //graficheaquilane
			String PATH_SCARICATI = "\\\\192.168.1.10\\Scaricati\\";
			//variabili ComandoPrintnet
			String NOME_LAVORAZIONE = "Programmi\\METAMER";
			String TIPO_LAVORAZIONE = "InserisciTacche";			
			String CARTELLA_DATI_INPUT = "";
			String NOME_FILE_INPUT = "";
			String TIPO_FORMATO_OUTPUT = "PDF";
			String NOME_MODULO_OUTPUT = "EPSON"; 
			Boolean SPLIT = false;
			String CARTELLA_DATI_OUTPUT = "\\\\192.168.1.8\\afp\\";
			String NOME_FILE_OUTPUT = ".%e";
			/*************************************************************************************/
			String lotto,nomeFilePdfFinale = null;
			PDFMergerUtility merger = new PDFMergerUtility();
			int contatorePDF = 0;
						
			//Mi prendo la data e l'ora odierna
			Date today = new Date();
			SimpleDateFormat DATE_FORMAT_LAVORATI = new SimpleDateFormat("yyyyMMdd");
			String date = DATE_FORMAT_LAVORATI.format(today);
			
			nomeFilePdfFinale = date + "_METAMER_MERGE_TACCHE.pdf"; 
			T_Lavori_Sottolotti sottolotti = new T_Lavori_Sottolotti(DATABASE);
			List<String> result = sottolotti.cercaLottiStampa("SELECT * FROM t_Lavori_Sottolotti WHERE IdCliente='405' AND (IdProdottoPostale='1' OR IdProdottoPostale='2') AND IdStatoLavorazione<2 GROUP BY LOTTO");
			sottolotti.chiudiConnessione();
			
			//Per ogni lotto trovato mi copio il file nella cartella temporanea
			for(int i=0;i<result.size();i++){
			    lotto = result.get(i);
			    String path_tmp = PATH_SCARICATI + lotto + "\\" + lotto + ".pdf";
			    contatorePDF++;
			    merger.addSource(path_tmp);
			} 
			
			if(contatorePDF>0){
				//Cerco l'ultimo progetto presente su db
				ComandoPrintNet inserisciTacche = new ComandoPrintNet(NOME_LAVORAZIONE, TIPO_LAVORAZIONE);
				String path_download = inserisciTacche.getPathProgetto().toString();
				path_download += "\\Dati\\";
				
				//Prima di scaricarci nuovi file pulisco la directory da eventuali file temporanei precedenti
				TemporaryDirectory download = new TemporaryDirectory(path_download);
				download.svuotaDirecdtory(download.getDirectoryName());
				
				merger.setDestinationFileName(path_download + nomeFilePdfFinale);
				merger.mergeDocuments();
				System.out.println("Ho mergiato " + contatorePDF + " file.");
				
				//Se va tutto ok lancio PrintNet
				NOME_FILE_OUTPUT = nomeFilePdfFinale + NOME_FILE_OUTPUT;
				inserisciTacche = new ComandoPrintNet(NOME_LAVORAZIONE, TIPO_LAVORAZIONE, CARTELLA_DATI_INPUT, NOME_FILE_INPUT, TIPO_FORMATO_OUTPUT, NOME_MODULO_OUTPUT, SPLIT, CARTELLA_DATI_OUTPUT, NOME_FILE_OUTPUT );
				new ComandoDOS(inserisciTacche.getComando());
				
				//Rimuovo i file temporanei
				download.svuotaDirecdtory(download.getDirectoryName());
				
			} else {
				System.out.println("Non ci sono file Metamer da stampare");
			}
			
			System.out.println("Copia File Metamer: Lavorazione terminata");
			
		} else System.out.println("Copia File Metamer: lavorazione esclusa dall'esecuzione");
	}// Chiudo il costruttore

}
