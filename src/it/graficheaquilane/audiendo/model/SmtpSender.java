package it.graficheaquilane.audiendo.model;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;

public class SmtpSender {
	
	private Properties props = new Properties();
	private Session session;
	
	public SmtpSender(String smtp){
		
		switch (smtp) {
		
			case "nuovaipa":
				
				this.props = new Properties();
			    props.put("mail.smtp.host", "smtpout.nuovaipa.com"); 
			    props.put("mail.smtp.port", "25"); 
			    props.put("mail.smtp.auth", "true");
			    
			    this.session = Session.getDefaultInstance(props,
		    	    new Authenticator() {
		    	        protected PasswordAuthentication  getPasswordAuthentication() {
		    	        return new PasswordAuthentication(
		    	                    "smtp@nuovaipa.com", "UbeVaC89");}
		    	    });
	
			    		    
				break;
				
			case "graficheaquilane":
				
				this.props = new Properties();
				props.put("mail.smtp.user","ced@graficheaquilane.it"); 
				props.put("mail.smtp.host", "smtp.aruba.it"); 
			    props.put("mail.smtp.auth", "true");
			    props.put("mail.smtp.starttls.enable","true"); 
			    props.put("mail.smtp.EnableSSL.enable","true");

			    props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");   
			    props.setProperty("mail.smtp.socketFactory.fallback", "false");   
			    props.setProperty("mail.smtp.port", "465");   
			    props.setProperty("mail.smtp.socketFactory.port", "465");
			    
			    this.session = Session.getDefaultInstance(props,
		    	    new Authenticator() {
		    	        protected PasswordAuthentication  getPasswordAuthentication() {
		    	        return new PasswordAuthentication(
		    	                    "ced@graficheaquilane.it", "graficheaquilane23");}
		    	    });

				
				break;
				
			case "gmail":

				this.props = new Properties();
				props.put("mail.smtp.host", "smtp.gmail.com"); 
			    props.put("mail.smtp.auth", "true");
			    props.put("mail.smtp.starttls.enable","true"); 
			    props.put("mail.smtp.EnableSSL.enable","true");

			    props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");   
			    props.setProperty("mail.smtp.socketFactory.fallback", "false");   
			    props.setProperty("mail.smtp.port", "465");   
			    props.setProperty("mail.smtp.socketFactory.port", "465");
			    
			    this.session = Session.getDefaultInstance(props,
		    	    new Authenticator() {
		    	        protected PasswordAuthentication  getPasswordAuthentication() {
		    	        return new PasswordAuthentication(
		    	                    "cesidio.a@gmail.com", "");}
		    	    });

				break;
	
			default:
				break;
		}
		
	}

	public Properties getProps() {
		return props;
	}

	public void setProps(Properties props) {
		this.props = props;
	}

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

}
