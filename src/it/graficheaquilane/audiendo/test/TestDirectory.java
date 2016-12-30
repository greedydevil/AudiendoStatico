package it.graficheaquilane.audiendo.test;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class TestDirectory {

	public static void main(String[] args) {
		File pathProgetto = new File("\\\\192.168.1.18\\Lavori IPA");
		String NomeLavorazione = "SED AQ";
		String TipoLavorazione = "Rateizzazione Bollettini";
		String anno,mese;
		
		//File[] result;
		
		switch (NomeLavorazione) {
			case "Comune di Roma":
				//Incremento il path con il nome della lavorazione
				switch (TipoLavorazione) {
					case "CRMSCS01":
						pathProgetto = new File(pathProgetto.getAbsoluteFile() + "\\" + NomeLavorazione + "\\" + "Scuole_CRMSCS01_XML");
						break;
					case "CRMISP01":
						pathProgetto = new File(pathProgetto.getAbsoluteFile() + "\\" + NomeLavorazione + "\\" + "Scuole_CRMISP01_ISP_XML");
						break;
					case "CRMCOS02":
						pathProgetto = new File(pathProgetto.getAbsoluteFile() + "\\" + NomeLavorazione + "\\" + "Scuole_CRMCOS02_Cosap_XML");
						break;
					case "CRMPDF01":
						pathProgetto = new File(pathProgetto.getAbsoluteFile() + "\\" + NomeLavorazione + "\\" + "Scuole_CRMPDF01_PDF");
						break;
					default:
						//System.out.println("sto nel default");
						pathProgetto = new File("");
						break;
				}
				
				System.out.println("Path vale: " + pathProgetto.getName());	
				
				anno = cercaAnno(pathProgetto);
				System.out.println("anno vale: " + anno);
				  
				break;
				
				case "SED AQ":
					
					//Incremento il path con il nome della lavorazione
					pathProgetto = new File(pathProgetto.getAbsoluteFile() + "\\" + NomeLavorazione + "\\" + TipoLavorazione);
									
					//System.out.println("Path vale: " + pathProgetto.getName());	
					
					anno = cercaAnno(pathProgetto);
					System.out.println("anno vale: " + anno);
					
					pathProgetto = new File(pathProgetto.getAbsoluteFile() + "\\" + anno);
					
					System.out.println("Path vale: " + pathProgetto.getAbsolutePath());
					
					mese = cercaMese(pathProgetto);
					System.out.println("mese vale: " + mese);
					
					break;
	
			default:
				break;
		}

	}
	
	public static String cercaAnno(File path){
		String[] children = path.list();
		ArrayList<String> listaAnni = new ArrayList<String>();
		
		for(String nomeFile : children){
			
			if(new File(path.getAbsoluteFile() + "\\" + nomeFile).isDirectory() && nomeFile.length()==4 ){
				//Se non è una cartella di lunghezza 4,cioè 4 lettere, la ignoro,altrimenti cerco la più grande
					listaAnni.add(nomeFile);
					System.out.println("Anni: " + nomeFile);	
			}
		}
		//Ordino per anno dal più piccolo al più grande
		Collections.sort(listaAnni);
		//System.out.println("Il più grande è: " + listaAnni.get(listaAnni.size()-1));
		return listaAnni.get(listaAnni.size()-1).toString();
	}
	
	public static String cercaMese(File path){
		String[] children = path.list();
		ArrayList<String> listaMesi = new ArrayList<String>();
		
		for(String nomeFile : children){
			
			if(new File(path.getAbsoluteFile() + "\\" + nomeFile).isDirectory()){
					listaMesi.add(nomeFile);
					System.out.println("Mesi: " + nomeFile);	
			}
		}
		//Ordino per anno dal più piccolo al più grande
		Collections.sort(listaMesi);
		//System.out.println("Il più grande è: " + listaAnni.get(listaAnni.size()-1));
		return listaMesi.get(listaMesi.size()-1).toString();
	}

}
