package it.graficheaquilane.audiendo.model;

public class Csv {
	
	private String path;
	private String[] result;
	
	public Csv(String path){
		
		this.path = path;		
	}

	public String getPath() {
		return path;
	}

	public String[] getResult() {
		return result;
	}
	

}
