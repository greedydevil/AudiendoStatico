package it.graficheaquilane.audiendo.exception;

public class FileNotFound extends Throwable {
	
	private static final long serialVersionUID = 1L;

	public FileNotFound(){
		System.out.println("Il file in esame non contiene nessuna delle tre parole cercate: 1)Lettera 2)Proposta 3)Informativa");
		
	}

}
