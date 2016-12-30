package it.graficheaquilane.audiendo;

import java.io.IOException;

import it.graficheaquilane.audiendo.implementation.MaranIfis;
import it.graficheaquilane.audiendo.progetti.ComuneRoma;

public class Main {

	public static void main(String[] args) throws IOException {
		
		/*
		 * Modalità: 
		 * 0)Lavorazione Disattivata
		 * 1)Debug mode 
		 * 2)Lavorazione Attiva
		 */
		
		MaranIfis ifis = new MaranIfis(0);
		
		ComuneRoma comune = new ComuneRoma(1);
		
		//LavorazioneTest test = new LavorazioneTest(2);
		
		//MaranSSunificato ss = new MaranSSunificato(1);
				
	}

}
