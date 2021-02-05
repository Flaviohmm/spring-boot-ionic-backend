package com.flaviomacedo.cursomc.services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;

import com.flaviomacedo.cursomc.domain.Pedido;

public abstract class AbstractEmailService implements EmailService {
	
	@Value("${default.sender}")
	private String sender;
	
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
}
