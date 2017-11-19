package br.com.ajafit.platform.core.integration;

import java.util.Properties;

import javax.mail.Address;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.jboss.logging.Logger;

public class SendMailIntegration {

	private static Logger logger = Logger.getLogger(SendMailIntegration.class);

	public static void send(MailMessage message) {

		try {
			final String smtpServer = "smtp.gmail.com";
			final String SOCKET_FACTORY = "javax.net.ssl.SSLSocketFactory";
			final String smtpPort = "587";
			final String PORT = "465";

			final String userAccount = System.getProperty("mail_user1");
			final String password = System.getProperty("mail_password1");

			final Properties props = new Properties();
			props.put("mail.smtp.host", smtpServer);
			props.put("mail.smtp.user", userAccount);
			props.put("mail.smtp.password", password);
			props.put("mail.smtp.port", smtpPort);
			props.put("mail.smtp.auth", true);
			props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.smtp.debug", "false");
			props.put("mail.smtp.socketFactory.port", PORT);
			props.put("mail.smtp.socketFactory.class", SOCKET_FACTORY);
			props.put("mail.smtp.socketFactory.fallback", "false");

			Session session = Session.getInstance(props, new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(userAccount, password);
				}
			});

			MimeMessage mimeMessage = new MimeMessage(session);
			final Address toAddress = new InternetAddress(message.getTo()); // toAddress
			final Address fromAddress = new InternetAddress(userAccount);
			mimeMessage.setContent(message.getMessage(), "text/html; charset=UTF-8");
			mimeMessage.setFrom(fromAddress);
			mimeMessage.setRecipient(javax.mail.Message.RecipientType.TO, toAddress);
			mimeMessage.setSubject(message.getSubject());
			Transport transport = session.getTransport("smtp");

			transport.connect(smtpServer, userAccount, password);
			transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());
			logger.info("mail sent: " + message);
		} catch (Exception e) {
			logger.error("mail :" + message, e);
		}

	}

}
