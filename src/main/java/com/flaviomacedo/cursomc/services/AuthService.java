package com.flaviomacedo.cursomc.services;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.flaviomacedo.cursomc.domain.Cliente;
import com.flaviomacedo.cursomc.repositories.ClienteRepository;
import com.flaviomacedo.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class AuthService {
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private BCryptPasswordEncoder passEncoder;
	
	@Autowired
	private EmailService emailService;
	
	private Random random = new Random();
	
	public void sendNewPassword(String email) {
		
		Cliente cliente = clienteRepository.findByEmail(email);
		if (cliente == null) {
			throw new ObjectNotFoundException("Email não encontrado");
		}
		
		String newPass = newPassword();
		cliente.setSenha(passEncoder.encode(newPass));
		
		clienteRepository.save(cliente);
		emailService.sendNewPasswordEmail(cliente, newPass);
	}

	private String newPassword() {
		char[] vetor = new char[10];
		for (int i = 0; i < 10; i++) {
			vetor[i] = randomChar();
		}
		return new String(vetor);
	}

	private char randomChar() {
		int opt = random.nextInt(3);
		if (opt == 0) { // Gera um Dígito
			return (char) (random.nextInt(10) + 48);
		} else if (opt == 1) { // Gera Letra Maiúscula
			return (char) (random.nextInt(26) + 65);
		} else { // Gera Letra Minúscula
			return (char) (random.nextInt(26) + 97);
		}
	}
}
