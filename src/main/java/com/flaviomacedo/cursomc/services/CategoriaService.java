package com.flaviomacedo.cursomc.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.flaviomacedo.cursomc.domain.Categoria;
import com.flaviomacedo.cursomc.repositories.CategoriaRepository;
import com.flaviomacedo.cursomc.services.exceptions.DataIntegrityException;
import com.flaviomacedo.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class CategoriaService {
	
	@Autowired
	private CategoriaRepository repository;
	
	public Categoria find(Integer id) {
		Optional<Categoria> obj = repository.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado! Id: " + id + ", Tipo: " + Categoria.class.getName()));
	}
	
	public Categoria insert(Categoria objeto) {
		objeto.setId(null);
		return repository.save(objeto);
	}
	
	public Categoria update(Categoria objeto) {
		find(objeto.getId());
		return repository.save(objeto);
	}
	
	public void delete(Integer id) {
		find(id);
		try {
			repository.deleteById(id);		
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possivel excluir uma categoria que possui produtos");
		}
	}
	
	public List<Categoria> findAll() {
		return repository.findAll();
	}
	
	public Page<Categoria> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		return repository.findAll(pageRequest);
	}
	
}
