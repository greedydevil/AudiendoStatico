package it.graficheaquilane.audiendo;

import java.io.IOException;

import it.graficheaquilane.audiendo.implementation.CopiaFileMaran;
import it.graficheaquilane.audiendo.implementation.CopiaFileMetamer;
import it.graficheaquilane.audiendo.implementation.RipristiniRomaCapitale;
import it.graficheaquilane.audiendo.progetti.MaranIfis;

public class Main {

	public static void main(String[] args) throws IOException {
		
		/*
		 * Modalità: 
		 * 0)Lavorazione Disattivata
		 * 1)Debug mode 
		 * 2)Lavorazione Attiva
		 */
		
		MaranIfis ifis = new MaranIfis(2);
		
		//Creo un file unico sulla base dei lotti caricati nella sottolotti
		CopiaFileMaran copiaFileMaran = new CopiaFileMaran(2);
		
		//Creo un file unico per metamer (aggiungo anche le tacche)
		CopiaFileMetamer copiaFileMetamer = new CopiaFileMetamer(2);
		
		//Gestisco i riprisitini di Roma Capitale
		RipristiniRomaCapitale rrc = new RipristiniRomaCapitale(0);
		
		
		//Test ciaooooo
		
		
		
		
		//ComuneRoma comuneRoma = new ComuneRoma(0);
		
		//LavorazioneTest test = new LavorazioneTest(2);
		
		//MaranSSunificato ss = new MaranSSunificato(1);
				
	}

}
