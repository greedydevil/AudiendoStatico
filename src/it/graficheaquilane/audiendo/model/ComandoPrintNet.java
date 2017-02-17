package it.graficheaquilane.audiendo.model;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

//Costruisce il comando PrintNet adeguato a seconda del tipo di lavorazione che si sta lanciando
public class ComandoPrintNet {
	
	String NomeLavorazione,TipoLavorazione,CartellaDatiInput,NomeFileInput,TipoFormatoOutput,NomeModuloOutput,CartellaDatiOutput,NomeFileOutput;
	Boolean Split;
	String Comando = "\"\"\\\\192.168.1.18\\PrintNet T Designer 7\\PNetTC.exe\""; //Path statico dove risede l'eseguibile di PrintNet - Comando CON apice iniziale
	//String Comando = "\"\\\\192.168.1.18\\PrintNet T Designer 7\\PNetTC.exe\""; //Path statico dove risede l'eseguibile di PrintNet - Comando SENZA apice iniziale
	String nomeProgetto,anno,mese,giorno;
	
	//Inizializzo il percorso del path completo
	File pathProgetto = new File("\\\\192.168.1.18\\Lavori IPA");
	
	//Costruttore generico per creare comandi sulla base di un progetto
	public ComandoPrintNet(String NomeLavorazione,String TipoLavorazione, String CartellaDatiInput, String NomeFileInput, String TipoFormatoOutput, String NomeModuloOutput, Boolean Split, String CartellaDatiOutput, String NomeFileOutput) {
		
		this.NomeLavorazione = NomeLavorazione;
		this.TipoLavorazione = TipoLavorazione;
		this.CartellaDatiInput = CartellaDatiInput;
		this.NomeFileInput = NomeFileInput;
		this.TipoFormatoOutput = TipoFormatoOutput;
		this.NomeModuloOutput = NomeModuloOutput;
		this.Split = Split;		
		this.CartellaDatiOutput = CartellaDatiOutput;
		this.NomeFileOutput = NomeFileOutput;
		
		CreaComando();
		
	}
	
	//Costruttore che cerca l'ultimo progetto sulla base del cliente e del tipo di lavorazione
	//Restituisce solo il path e non l'ultimo progetto
	public ComandoPrintNet(String NomeLavorazione, String TipoLavorazione){
		this.NomeLavorazione = NomeLavorazione;
		this.TipoLavorazione = TipoLavorazione;
		
		CercaPathUltimoProgetto();
	}
	
	
	public void CreaComando(){
		
		//Inizializzo il percorso del path completo
		//File pathProgetto = new File("\\\\192.168.1.18\\Lavori IPA");
		
		if(NomeLavorazione=="Comune di Roma")
			this.TipoLavorazione = CercaTipoLavorazione(NomeLavorazione, TipoLavorazione);		
		
		//A questo punto mi cambia il path
		pathProgetto = new File(pathProgetto.getAbsoluteFile() + "\\" + this.NomeLavorazione + "\\" + this.TipoLavorazione);
		
		//Ora mi cerco l'anno pi� recente all'interno della stessa lavorazione
		this.anno = CercaElementoMax(pathProgetto, 0);
		pathProgetto = new File(pathProgetto.getAbsoluteFile() + "\\" + anno);
		
		//Cerco l'ultimo mese presente
		this.mese = CercaElementoMax(pathProgetto, 1);
		pathProgetto = new File(pathProgetto.getAbsoluteFile() + "\\" + mese);
		
		//Cerco il giorno pi� recente
		this.giorno = CercaElementoMax(pathProgetto, 1);
		pathProgetto = new File(pathProgetto.getAbsoluteFile() + "\\" + giorno);
		
		//Cerco il progetto pi� recente
		this.nomeProgetto = CercaProgetto(pathProgetto);
		pathProgetto = new File(pathProgetto.getAbsoluteFile() + "\\" + nomeProgetto);
		
		//System.out.println("Path vale: " + pathProgetto.getAbsolutePath());
		
		//Creo il comando da dare a PrintNet
		String PnetCommand = "\"" + pathProgetto.getAbsolutePath() + "\"";
		//System.out.println("Pnet2: " + PnetCommand);
		Comando += (" " + PnetCommand);
		
		//Se gli passo una cartellaDatiInput ed un nomeFileInput vuol dire che devo aggiungere al comando le istruzioni
		if(CartellaDatiInput!=""){
			if(NomeFileInput=="") System.err.println("ComandoPrintNet - E' stata fornita una directory di input senza specificare il nome del file di input");
			else Comando += " -difNominativi " + CartellaDatiInput + "\\" + NomeFileInput;
		}
		
		//Aggiungo al comando il TipoFormatoOutput (qualora specificato),cio� se PDF,AFP,PnetNative etc
		if(TipoFormatoOutput!="") Comando += (" -e " + TipoFormatoOutput);
		
		
		//Aggiungo se c'� il NomeModuloOutput ossia il nome del modulo di output nel progetto,cio� AFP,AutoDraft etc.Il tipo vuole gli apici doppi es: "AFP"
		if(NomeModuloOutput!="") Comando += (" -o " + "\"" + NomeModuloOutput + "\""); 
		
		
		//Aggiungo splitByGroup se c'�
		if(Split) Comando += (" -splitbygroup");
		
		
		//Se gli passo una CartellaDatiOutput ed un NomeFileOutput lo devo aggiungere al comando
		if(CartellaDatiOutput!=""){
			if(NomeFileOutput=="") System.err.println("ComandoPrintNet - E' stata fornita una directory di output senza specificare il nome del file di output");
			else Comando += " -f " + "\"" + CartellaDatiOutput + "\\" + NomeFileOutput + "\"";
		}
		
		//Aggiungo il tag " di chiusura del comando
		Comando += "\"";
		//System.out.println("Comando:" + Comando);
		
	}
	
	//Cerco il nome del progetto dato il cliente (NomeLavorazione)  
	public String CercaTipoLavorazione(String nomeLavorazione,String tipoLavorazione){
		
		String nomeProgettoApp="";
		
		switch (NomeLavorazione) {
		
			case "Comune di Roma":
				//Incremento il path con il nome della lavorazione
				switch (TipoLavorazione) {
					case "CRMSCS01":
						//pathProgetto = new File(pathProgetto.getAbsoluteFile() + "\\" + NomeLavorazione + "\\" + "Scuole_CRMSCS01_XML");
						nomeProgettoApp = "Scuole_CRMSCS01_XML";
						break;
					case "CRMISP01":
						nomeProgettoApp = "Scuole_CRMISP01_ISP_XML";
						break;
					case "CRMCOS02":
						nomeProgettoApp = "Scuole_CRMCOS02_Cosap_XML";
						break;
					case "CRMPDF01":
						nomeProgettoApp = "Scuole_CRMPDF01_PDF";
						break;
					default:
						System.err.println("ComandoPrintNet - Il path del progetto � vuoto!Il tipo di lavorazione non � corretto");
						break;
				}
			break;
			
			default:
				break;
		}
		
		return nomeProgettoApp;
	}
	
	//Cerco l'elemento pi� grande all'interno di una lista che sia l'anno, cio� il pi� recente, o il mese o il giorno
	//con typeSearch=0 cerco l'anno,con 1 cerco il mese o il giorno
	public String CercaElementoMax(File path,int typeSearch){
		
		String[] children = path.list();
		ArrayList<String> listaElementi = new ArrayList<String>();
		
		for(String nomeFile : children){
			switch(typeSearch){
				case 0: //Cerco un anno,cio� una stringa di 4 digit
					//Se � una directory ed � di lunghezza 4,cio� 4 lettere,cerco la pi� grande altrimenti non � una cartella "anno"
					if(new File(path.getAbsoluteFile() + "\\" + nomeFile).isDirectory() && nomeFile.length()==4 )
						listaElementi.add(nomeFile);
					break;
				
				case 1: //Cerco un mese o un giorno
					//Se � una directory ed � di lunghezza 4,cio� 4 lettere,cerco la pi� grande altrimenti non � una cartella "anno"
					if(new File(path.getAbsoluteFile() + "\\" + nomeFile).isDirectory())
						listaElementi.add(nomeFile);					
					break;
				
				default: 
					System.err.println("ComandoPrintNet - Errore nel passaggio del tipo di ricerca in CercaElementoMax");
					
					break;
			}
			
		}
		
		//Ordino per anno dal pi� piccolo al pi� grande
		Collections.sort(listaElementi);
		
		//System.out.println("Il pi� grande �: " + listaAnni.get(listaAnni.size()-1));
		return listaElementi.get(listaElementi.size()-1).toString();
	}
	
	//Cerco il progetto pi� recente,cio� quelli con estensione .wfd e che non contengono la scritta copia
	//Questo metodo � usato se si conosce gi� il path completo (giorno,mese,anno...)
	public String CercaProgetto(File path){
		
		String[] children = path.list();
		Boolean trovato = false;
		String nomeProgetto = "";
				
		for(String nome : children){			
			if(new File(path.getAbsoluteFile() + "\\" + nome).isFile() && (!nome.contains("Copia") || !nome.contains("copia")) && nome.contains(".wfd") ){
				nomeProgetto = nome;
				trovato = true;
			}
		}
		if(!trovato) System.err.println("ComandoPrintNet - Attenzione non � stato individuato un progetto per questa lavorazione!");
		
		return nomeProgetto;
	}
	
	public void CercaPathUltimoProgetto(){
		
		//File path_tmp = this.pathProgetto;
		//String anno_tmp,mese_tmp,giorno_tmp;
			
		if(NomeLavorazione=="Comune di Roma")
			TipoLavorazione = CercaTipoLavorazione(NomeLavorazione, TipoLavorazione);		
		
		//A questo punto mi cambia il path
		pathProgetto = new File(pathProgetto.getAbsoluteFile() + "\\" + NomeLavorazione + "\\" + TipoLavorazione);
		
		//Ora mi cerco l'anno pi� recente all'interno della stessa lavorazione
		anno = CercaElementoMax(pathProgetto, 0);
		pathProgetto = new File(pathProgetto.getAbsoluteFile() + "\\" + anno);
		
		//Cerco l'ultimo mese presente
		mese = CercaElementoMax(pathProgetto, 1);
		pathProgetto = new File(pathProgetto.getAbsoluteFile() + "\\" + mese);
		
		//Cerco il giorno pi� recente
		giorno = CercaElementoMax(pathProgetto, 1);
		pathProgetto = new File(pathProgetto.getAbsoluteFile() + "\\" + giorno);
		
		//return pathProgetto.toString();
	}
		
	public String getComando() {
		return Comando;
	}

	public void setComando(String comando) {
		Comando = comando;
	}

	public File getPathProgetto() {
		return pathProgetto;
	}
	
}
