package it.graficheaquilane.audiendo.test;

import java.util.Collection;
import java.util.Vector;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import it.graficheaquilane.audiendo.model.ComandoDOS;
import it.graficheaquilane.audiendo.model.ComandoPrintNet;

public class Main {

	public static void main(String[] args) {
		/*
		String NomeLavorazione = "Maran";
		String TipoLavorazione = "Prestitalia";
		
		//String NomeLavorazione = "Comune di Roma";
		//String TipoLavorazione = "CRMSCS01";
		//Valori: NomeLavorazione, TipoLavorazione, CartellaDatiInput, NomeFileInput, TipoFormatoOutput, NomeModuloOutput, Split, CartellaDatiOutput, NomeFileOutput
		ComandoPrintNet cp = new ComandoPrintNet(NomeLavorazione, TipoLavorazione, "C:\\TEST", "test.csv","PDF","AFP",false,"C:\\AFP","Output.pdf");
		System.out.println("Comando vale: " + cp.getComando());
		*/
		ComandoPrintNet cpRoma = new ComandoPrintNet("Comune di Roma", "CRMSCS01", "C:\\TEST", "test.csv","PDF","AFP",false,"C:\\AFP","Output.pdf");
		//System.out.println("Comando vale: " + cpRoma.getComando());
		ComandoDOS cd = new ComandoDOS(cpRoma.getComando());
		
		/*
		 JTable it = new JTable();
		
		DefaultTableModel model = new DefaultTableModel();
		model = (DefaultTableModel) it.getModel();
		Vector<Collection<String>> vs = new Vector<>();
		vs.addElement("5");
		model.addRow("4");
		*/

	}

}
