package com.flaviomacedo.cursomc.services;

import java.util.Date;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.flaviomacedo.cursomc.domain.Pedido;

public abstract class AbstractEmailService implements EmailService {
	
	@Value("${default.sender}")
	private String sender;
	
	@Autowired
	private TemplateEngine templateEngine;
	
	@Autowired
	private JavaMailSender javaMailSender;
	
	@Override
	public void sendOrderConfirmationEmail(Pedido objeto) {
		SimpleMailMessage message = prepareSimpleMailMessageFromPedido(objeto);
		sendEmail(message);
	}

	protected SimpleMailMessage prepareSimpleMailMessageFromPedido(Pedido objeto) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(objeto.getCliente().getEmail());
		message.setFrom(sender);
		message.setSubject("Pedido Confirmado! Código: " + objeto.getId());
		message.setSentDate(new Date(System.currentTimeMillis()));
		message.setText(objeto.toString());
		return message;
	}
	
	protected String htmlFromTemplatePedido(Pedido objeto) {
		Context context = new Context();
		context.setVariable("pedido", objeto);
		return templateEngine.process("email/confirmacaoPedido", context);
	}
	
	@Override
	public void sendOrderConfirmationHtmlEmail(Pedido objeto) {
		try {
			MimeMessage mimeMessage = prepareMimeMessageFromPedido(objeto);
			sendHtmlEmail(mimeMessage);
		} catch (MessagingException e) {
			sendOrderConfirmationEmail(objeto);
		}
	}

	protected MimeMessage prepareMimeMessageFromPedido(Pedido objeto) throws MessagingException {
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
		helper.setTo(objeto.getCliente().getEmail());
		helper.setFrom(sender);
		helper.setSubject("Pedido Confirmado! Código: " + objeto.getId());
		helper.setSentDate(new Date(System.currentTimeMillis()));
		helper.setText(htmlFromTemplatePedido(objeto), true);
		return mimeMessage;
	}
}
