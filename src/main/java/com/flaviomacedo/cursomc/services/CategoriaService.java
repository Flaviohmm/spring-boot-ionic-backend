package com.flaviomacedo.cursomc.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.flaviomacedo.cursomc.domain.Categoria;
import com.flaviomacedo.cursomc.repositories.CategoriaRepository;

@Service
public class CategoriaService {
	
	@Autowired
	private CategoriaRepository repository;
	
	public Optional<Categoria> buscar(Integer id) {
		Optional<Categoria> obj = repository.findById(id);
		return obj;
	}

}
