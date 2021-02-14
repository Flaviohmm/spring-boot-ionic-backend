package com.flaviomacedo.cursomc.services;

import org.springframework.mail.SimpleMailMessage;

import com.flaviomacedo.cursomc.domain.Cliente;
import com.flaviomacedo.cursomc.domain.Pedido;

public interface EmailService {
	
	void sendOrderConfirmationEmail(Pedido objeto);
	
	void sendEmail(SimpleMailMessage message);
	
	void sendNewPasswordEmail(Cliente cliente, String newPass);
}
