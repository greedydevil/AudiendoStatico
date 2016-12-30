package it.graficheaquilane.audiendo.model;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ComandoDOS {
	
	Process process = null;
	String command="cmd.exe /c ",line = null,resultCommand = "";
	Boolean result = false;
	
	public ComandoDOS(String comando){
		
		this.command += comando;
		try {
			System.out.println("Attendere...esecuzione comando " + comando + " in corso...");
			process = Runtime.getRuntime().exec(command);	
			InputStream stderr = process.getInputStream();
            InputStreamReader isr = new InputStreamReader(stderr);
            BufferedReader br = new BufferedReader(isr);           
            
            //System.out.println("<ERROR>");
            while ( (line = br.readLine()) != null){
                //System.out.println(line);
                resultCommand += line + "\n";
            }
            //System.out.println("</ERROR>");
            int exitVal = process.waitFor();
            //System.out.println("Process exitValue: " + exitVal);
            //System.out.println("Line:" + resultCommand);
            if(exitVal==0){
            	result = true;
            	System.out.println("ComandoDOS - Ho eseguito correttamente il comando:" + comando);
            }
            else System.out.println("ComandoDOS - Il comando eseguito ha restituito il seguente errore: \n" + getResultCommand());
            
		
		} catch (Exception e) {
			System.out.println("ComandoDOS - Qualcosa è andato storto!");
			System.out.println(resultCommand);
			e.printStackTrace();
		}
		//System.out.println("ho terminato la lavorazione");
				
	}

	public String getCommand() {
		return command;
	}
	
	public Boolean getResult() {
		return result;
	}
	
	public String getResultCommand(){
		return resultCommand;
	}

}
