package pw.proj.letsmeet.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import pw.proj.letsmeet.config.ApplicationProperties;
import pw.proj.letsmeet.model.User;
import pw.proj.letsmeet.service.EmailService;

@Service
public class EmailServiceImpl implements EmailService {

	@Autowired
	public JavaMailSender emailSender;

	@Autowired
	private ApplicationProperties applicationProperties;

	@Override
	public void sendPasswordRemindEmail(User user) {
		String url = applicationProperties.getFrontendAddress() + "/change-password?resetPasswordToken=" + user.getResetPasswordToken();
		String text = "Kliknij w poniższy link aby ustawić nowe hasło: " + url;

		this.sendSimpleMessage(user.getEmail(), "LetsMeet - Przypomnienie hasła", text);
	}

	private void sendSimpleMessage(String to, String subject, String text) {

		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(to);
		message.setSubject(subject);
		message.setText(text);
		emailSender.send(message);
	}

}
