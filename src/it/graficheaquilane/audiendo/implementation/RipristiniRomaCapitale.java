package it.graficheaquilane.audiendo.implementation;

import java.io.IOException;
import java.util.List;

import javax.swing.JOptionPane;

import it.graficheaquilane.audiendo.model.ComandoDOS;
import it.graficheaquilane.audiendo.model.ComandoPrintNet;
import it.graficheaquilane.audiendo.query.T_Lavori_FDL;

//Prende i file dei lotti caricati sulla t_sottolotti (stato=1) e crea un file unico per la stampa
public class RipristiniRomaCapitale {

	
	public RipristiniRomaCapitale(int debug) throws IOException{
						
		if(debug!=0){
			
			System.out.println("");
			System.out.println("Inizio lavorazione Ripristini Roma Capitale");
			
			/****************** COSTANTI - CAMBIARE IN CASO DI CAMBIO INDIRIZZO ******************/
			String DATABASE = "graficheaquilane"; // graficheaquilane
			//variabili ComandoPrintnet
			String NOME_LAVORAZIONE = "Programmi\\Audiendo Impasta TNO";
			String TIPO_LAVORAZIONE = "Roma Capitale";			
			String CARTELLA_DATI_INPUT = "";
			String NOME_FILE_INPUT = "";
			String TIPO_FORMATO_OUTPUT = "PDF";
			String NOME_MODULO_OUTPUT = "Output"; 
			Boolean SPLIT = true;
			//String CARTELLA_DATI_OUTPUT = "\\\\192.168.1.8\\afp\\";
			String CARTELLA_DATI_OUTPUT = "\\\\192.168.1.8\\afp\\";
			String NOME_FILE_OUTPUT = "%h[3].%e";
			
			/*************************************************************************************/
			
			String macrolotto = JOptionPane.showInputDialog(null, "Inserisci il macrolotto da cercare");			
			//System.out.println("s vale:" + s);
			//test "20170126125916"
			
			//Mi prendo la data e l'ora odierna
			//Date today = new Date();
			//SimpleDateFormat DATE_FORMAT_LAVORATI = new SimpleDateFormat("yyyy-MM-dd");
			//String dataRistampa = DATE_FORMAT_LAVORATI.format(today);
			
			T_Lavori_FDL t_fdl = new T_Lavori_FDL(DATABASE);
			//Cerco il FDL da ristampare
			List<String> result = t_fdl.cercaFDL("SELECT * FROM t_Lavori_FDL WHERE MacroLottoAudiendo LIKE '%" + macrolotto + "%' GROUP BY FDL");
			String FDL = result.get(0);
			System.out.println("Il FDL da ripristinare è il numero: " + FDL);
			
			/************************ UPDATE *************************************/
			//Se la lavorazione è attiva setto a 1 la ristampa ed inserisco la data di ristampa
			if(debug==2) {
				t_fdl.setRistampaFDL(FDL);
				System.out.println("Ho aggiornato il FDL. Ora è in ristampa!");
			}
			/*********************************************************************/
			
			ComandoPrintNet ripristiniRomaCapitale = new ComandoPrintNet(NOME_LAVORAZIONE, TIPO_LAVORAZIONE, CARTELLA_DATI_INPUT, NOME_FILE_INPUT,TIPO_FORMATO_OUTPUT,NOME_MODULO_OUTPUT,SPLIT,CARTELLA_DATI_OUTPUT, NOME_FILE_OUTPUT);
			new ComandoDOS(ripristiniRomaCapitale.getComando());
			
			
			
			
			
			/*
			
			
			
			
			
			
			String lotto,nomeFilePdfFinale = null;
			PDFMergerUtility merger = new PDFMergerUtility();
			int contatorePDF = 0;
						
			//Mi prendo la data e l'ora odierna
			Date today = new Date();
			SimpleDateFormat DATE_FORMAT_LAVORATI = new SimpleDateFormat("yyyyMMdd");
			String date = DATE_FORMAT_LAVORATI.format(today);
			
			nomeFilePdfFinale = date + "_METAMER_MERGE_TACCHE.pdf"; 
			T_Lavori_Sottolotti sottolotti = new T_Lavori_Sottolotti("graficheaquilane");
			List<String> result = sottolotti.cercaLottiStampa("SELECT * FROM t_Lavori_FDL WHERE IdCliente='405' AND (IdProdottoPostale='1' OR IdProdottoPostale='2') AND IdStatoLavorazione<2 GROUP BY LOTTO");
			
			//Per ogni lotto trovato mi copio il file nella cartella temporanea
			for(int i=0;i<result.size();i++){
			    lotto = result.get(i);
			    String path_tmp = PATH_SCARICATI + lotto + "\\" + lotto + ".pdf";
			    contatorePDF++;
			    merger.addSource(path_tmp);
			} 
			
			if(contatorePDF>0){
				//Cerco l'ultimo progetto presente su db
				ComandoPrintNet inserisciTacche = new ComandoPrintNet("METAMER", "InserisciTacche");
				String path_download = inserisciTacche.getPathProgetto().toString();
				path_download += "\\Dati\\";
				
				//Prima di scaricarci nuovi file pulisco la directory da eventuali file temporanei precedenti
				TemporaryDirectory download = new TemporaryDirectory(path_download);
				download.svuotaDirecdtory(download.getDirectoryName());
				
				merger.setDestinationFileName(path_download + nomeFilePdfFinale);
				merger.mergeDocuments();
				System.out.println("Ho mergiato " + contatorePDF + " file.");
				
				//Se va tutto ok lancio PrintNet
				inserisciTacche = new ComandoPrintNet("METAMER", "InserisciTacche", "", "","PDF","EPSON",false,PATH_FILE_OUTPUT, nomeFilePdfFinale + ".%e");
				//ComandoDOS servicePnet = 
				new ComandoDOS(inserisciTacche.getComando());
				
				//Rimuovo i file temporanei
				download.svuotaDirecdtory(download.getDirectoryName());
				
			} else {
				System.out.println("Non ci sono file del Comune di Roma da ripristinare");
			}
			*/
			System.out.println("Ripristini Roma Capitale: Lavorazione terminata");
			
		} else System.out.println("Ripristini Roma Capitale: lavorazione esclusa dall'esecuzione");
	}// Chiudo il costruttore

}
