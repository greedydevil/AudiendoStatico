package it.graficheaquilane.audiendo.testMarcogay;

import java.awt.Color;
import java.awt.Component;
import java.awt.SystemColor;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class ProvaJTable {
   
   //colori che utiliuzzeremo per colorare la tabella
   public final static Color selezione = SystemColor.textHighlight;
   public final static Color colorato = Color.GREEN;
   public final static Color focus = Color.YELLOW;
   
   
   public static void main(String[] args) {
       //rappresenta le celle della nostra tabella
       Object[][] rowData = new Object[][] {
               { "uno", "due", "tre", "quattro"},
               { "cinque", "sei", "sette", "otto"},
               { "nove", "dieci", "undici", "dodici"},
               { "tredici", "quattordici", "quindici", "sedici"}
       };
       
       //le celle che hanno valore true sono da colorare
       final boolean[][] daColorare = new boolean[][] {
               { true, false, false, false },
               { false, true , true, false },
               { false, true, false, false },
               { false, false, false, false}
       };
       
       //nomi delle colonne
       String[] columnNames = new String[] { "1^ colonna", "2^ colonna", "3^ colonna", "4^ colonna" };

       //creiamo la tabella
       JTable table = new JTable(rowData, columnNames);
       
       /*
        * Qui si imposta il renderer della tabella. Pensa al renderer come il componente
        * che si occupa di visualizzare a schermo le informazioni contenute nella nostra
        * tabella.
        * Quando viene visualizzata a schermo ogni cella di una tabella è un componente
        * che rappresenta visualimente ciò che la cella stessa contiene.
        *
        * Un renderer avente come componente una JLabel, per esempio, può rappresentare
        * gli oggetti testuali (stringhe, numeri...)
        *
        * Un renderer avente come componente un JButton, sarà un pulsante
        *
        * Come parametri, Object.class significa che tutti gli oggetti della cella
        * che derivono dalla classe Object avranno il renderer che specificheremo
        * (in una tabella tutto deriva dalla classe Object).
        *
        * new TableCellRenderer() è un'interfaccia
        */
       table.setDefaultRenderer(Object.class, new TableCellRenderer() {
           //metodo dell'interfaccia
           @Override
           public Component getTableCellRendererComponent(JTable table,
                   Object value, boolean isSelected, boolean hasFocus,
                   int row, int column){
               //dobbiamo rappresentare oggetti testuali, quindi ci serve una JLabel
               JLabel label = new JLabel(value.toString());
               //appena creata una jlabel non può avere lo sfondo (o meglio, è trasparente);
               //per renderlo possibile rendiamo la nostra label opaca
               label.setOpaque(true);
               //le assegnamo lo sfondo che una cella non selezionata e non colorata dovrà avere
               label.setBackground(Color.WHITE);
               
               //se la riga della nostra cella è selezionata...
               if (isSelected) {
                   // e la cella è da colorare...
                   if (daColorare[row][column]) {
                       label.setBackground(
                           //il suo sfondo sarà un miscuglio del colore di selezione
                           //e della colore di colorazione
                           coloreIntermedio(selezione, colorato)
                       );
                   }else{
                       //se invece la riga è selzionata ma la cella non è da colorare
                       //impostiamo lo sfondo a colore "selezione"
                       label.setBackground(selezione);
                   }
                   
                   //se la riga non è selezionata ma la cella è comunque da colorare
               } else if (daColorare[row][column]) {
                   label.setBackground(colorato);
               }
               
               //se la riga non è selezionata e la cella non è da colorare il suo
               //sfondo rimane bianco come abbimamo impostato prima
               
               
               //la cella che ha il focus è la cella selezionata
               //isSelected, ci informa che la cella appartiene o meno alla riga selezionata
               //hasFocus invece specifica se la cella è letteralmente selezionata
               if(hasFocus){
                   //se la cella è selezionata, sovrascriviamo il suo sfondo
                   //attuale e rendiamola di colore "focus"
                   label.setBackground(focus);
               }
               
               //alla fine ritorniamo la label (come componente cher si occupa di rappresentare
               //grafica mente la cella)
               return label;
               
               //il metodo getTableCellRendererComponent viene eseguito per ogni cella della tabella
           }
       });
       
       //visualizzaimo la tabella
       JFrame frame = new JFrame();
       frame.add(table);
       frame.pack();
       frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       frame.setVisible(true);
   }

   //metodo che crea un colore intermedio tra 2
   public static Color coloreIntermedio(Color colore1, Color colore2) {
       return new Color(
               (colore1.getRed() + colore2.getRed()) / 2,
               (colore1.getGreen() + colore2.getGreen()) / 2,
               (colore1.getBlue() + colore2.getBlue()) / 2);
   }

}