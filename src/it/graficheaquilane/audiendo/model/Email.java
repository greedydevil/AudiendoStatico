package it.graficheaquilane.audiendo.model;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class Email {
	
	private String mittente;
	private String destinatario;
	private String cc;
	private String oggetto;
	private String messaggio;
		
	//Costruttore per un destinatario
	public Email(String mittente,String destinatario,String oggetto,String messaggio){
		this.mittente = mittente;
		this.destinatario = destinatario;
		this.oggetto = oggetto;
		this.messaggio = messaggio;
	}
	
	//Costruttore per più destinatari (vengono separati da un ;)
	public Email(String mittente,String destinatario,String cc,String oggetto,String messaggio){
		this.mittente = mittente;
		this.destinatario = destinatario;
		this.cc = cc;
		this.oggetto = oggetto;
		this.messaggio = messaggio;		
	}
	
	public String getMittente() {
		return mittente;
	}

	public void setMittente(String mittente) {
		this.mittente = mittente;
	}

	public String getDestinatario() {
		return destinatario;
	}

	public void setDestinatario(String destinatario) {
		this.destinatario = destinatario;
	}

	public String getCc() {
		return cc;
	}

	public void setCc(String cc) {
		this.cc = cc;
	}

	public String getOggetto() {
		return oggetto;
	}

	public void setOggetto(String oggetto) {
		this.oggetto = oggetto;
	}

	public String getMessaggio() {
		return messaggio;
	}

	public void setMessaggio(String messaggio) {
		this.messaggio = messaggio;
	}
	
	public void stampaEmail(){		
		if(this.cc!=null) System.out.println("Mittente: " + mittente + " Destinatario: " + destinatario + " CC:" + cc + " Oggetto: " + oggetto + " Messaggio:" + messaggio);
		else System.out.println("Mittente: " + mittente + " Destinatario: " + destinatario + " Oggetto: " + oggetto + " Messaggio:" + messaggio);
	}
	
	
	public void sendMail () {		
		//Prendo il mittente e ne ricavo il dominio
		String[] tmp = mittente.split("@");
		String dominio = tmp[1];
		dominio = dominio.replace(".it", "");
		dominio = dominio.replace(".com", "");
		
		SmtpSender service = new SmtpSender(dominio);
		//Properties props = service.getProps();
		Session session = service.getSession();
		
	    // Creazione del messaggio da inviare
	    MimeMessage message = new MimeMessage(session);
	    try {
			message.setSubject(this.oggetto);
			message.setText(this.messaggio);

		    // Aggiunta degli indirizzi del mittente e del destinatario
		    InternetAddress fromAddress = new InternetAddress(this.mittente);
		    InternetAddress toAddress = new InternetAddress(this.destinatario);
		    message.setFrom(fromAddress);
		    message.setRecipient(Message.RecipientType.TO, toAddress);
		    
		    if(this.cc != null){
		    	//Scorro l'array dei cc una volta esploso dal ;
		    	String[] listCC = this.cc.split(";");
		    	for(int j=0;j<listCC.length;j++){
			    	InternetAddress[] myCcList = InternetAddress.parse(listCC[j]);
			    	message.addRecipients(Message.RecipientType.CC, myCcList);
		    	}
		    }

		    // Invio del messaggio
		    Transport.send(message);

		} catch (MessagingException e) {
			System.out.println("Errore nell'invio dell'email");
			e.printStackTrace();
		}
	}
	
}
