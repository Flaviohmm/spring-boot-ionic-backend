package com.flaviomacedo.cursomc.services;

import org.springframework.mail.SimpleMailMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MockEmailService extends AbstractEmailService {
	
	private static final Logger LOG = LoggerFactory.getLogger(MockEmailService.class);

	@Override
	public void sendEmail(SimpleMailMessage message) {
		LOG.info("Simulando Envio de Email...");
		LOG.info(message.toString());
		LOG.info("Email Enviado");
	}
}
