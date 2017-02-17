package it.graficheaquilane.audiendo.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class TemporaryDirectory {
	
	File nomeDirectory;
	String originalName;
	
	public TemporaryDirectory(String nomeDirectory){		
		this.nomeDirectory = new File(nomeDirectory);
		this.originalName = nomeDirectory;
	}
	
	public void createTemporaryDir(){
		
		if(verifyExistDir()) {
			//System.out.println("La cartella che si stà creando già esiste!");
			return;
		} else{
			if (!nomeDirectory.mkdir()) 
				System.out.println("createTemporaryDir - Impossibile creare: " + nomeDirectory + " Qualcosa è andato storto!");
		}
		
		
	}
	
	public boolean verifyExistDir(){
		
		if(nomeDirectory.exists()) return true;
		else return false;
		
	}
	
	public void removeTemporaryDir(){
		
		recorsiveDeleteDirecdtory(nomeDirectory);
		
	}
	
	public void recorsiveDeleteDirecdtory(File path){
		
		if(path.exists()) {
            File[] files = path.listFiles();
            
            for(int i=0;i<files.length;i++){
            	if(files[i].isDirectory()) {
            		recorsiveDeleteDirecdtory(files[i]);
                }
                else {
                    files[i].delete();
                }
            }
	    }
	    path.delete();
	}
	
	public void svuotaDirecdtory(File nomeDirectory){
		
		if(nomeDirectory.exists()) {
            File[] files = nomeDirectory.listFiles();
            
            for(int i=0;i<files.length;i++){
            	if(files[i].isDirectory()) {
            		svuotaDirecdtory(files[i]);
                }
                else {
                    files[i].delete();
                }
            }
	    }
	    //path.delete();
	}

	public File getDirectoryName() {
		return nomeDirectory;
	}
	
	public void copyFile(String source,String destination){
		File s = new File(source);
		File d = new File(destination);
		copyFileStreams(s,d);		
	}

	private static void copyFileStreams(File source, File dest) {
	    InputStream input = null;
		OutputStream output = null;
		try {
	        input = new FileInputStream(source);
	        output = new FileOutputStream(dest);
	        byte[] buf = new byte[1024];
	        int bytesRead;
	        while ((bytesRead = input.read(buf)) > 0) {
	            output.write(buf, 0, bytesRead);
	        }
	    } catch (FileNotFoundException e) {
			System.out.println("copyFileStreams - Errore nella copia del file");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("copyFileStreams - Errore nell'output");
			e.printStackTrace();
		} finally {
	    	try {
				input.close();
				output.close();
			} catch (IOException e) {
				System.out.println("copyFileStreams - Errore nella chiusura dei file input e output");
				e.printStackTrace();
			}
	    	
	    }
	}
	
	public void moveFile(String source,String destination){
		
		InputStream inStream = null;
		OutputStream outStream = null;
			
	    	try{
	    		
	    	    File afile = new File(source);
	    	    File bfile = new File(destination);
	    		
	    	    inStream = new FileInputStream(afile);
	    	    outStream = new FileOutputStream(bfile);
	        	
	    	    byte[] buffer = new byte[1024];
	    		
	    	    int length;
	    	    //copy the file content in bytes 
	    	    while ((length = inStream.read(buffer)) > 0)	    	  
	    	    	outStream.write(buffer, 0, length);
	    	 
	    	    inStream.close();
	    	    outStream.close();
	    	    
	    	    //delete the original file
	    	    afile.delete();
	    	    
	    	}catch(IOException e){
	    		System.out.println("MoveFile - Errore nello spostamento del file!");
	    	    e.printStackTrace();
	    	}
		
		
	}
	
	public String[] getListFileDir(){
		String result[] = nomeDirectory.list();
		return result;
		
	}

	public String getOriginalName() {
		return originalName;
	}

	public void setOriginalName(String originalName) {
		this.originalName = originalName;
	}
	
	
}
