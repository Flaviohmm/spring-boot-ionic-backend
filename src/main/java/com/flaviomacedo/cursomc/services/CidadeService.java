package com.flaviomacedo.cursomc.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.flaviomacedo.cursomc.domain.Cidade;
import com.flaviomacedo.cursomc.repositories.CidadeRepository;

@Service
public class CidadeService {
	
	@Autowired
	private CidadeRepository repository;
	
	public List<Cidade> findByEstado(Integer estadoId) {
		return repository.findCidades(estadoId);
	}
}
