package com.flaviomacedo.cursomc.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.flaviomacedo.cursomc.domain.Cliente;
import com.flaviomacedo.cursomc.dto.ClienteDTO;
import com.flaviomacedo.cursomc.repositories.ClienteRepository;
import com.flaviomacedo.cursomc.services.exceptions.DataIntegrityException;
import com.flaviomacedo.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class ClienteService {

	@Autowired
	private ClienteRepository repository;

	public Cliente find(Integer id) {
		Optional<Cliente> obj = repository.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Cliente.class.getName()));
	}

	public Cliente update(Cliente objeto) {
		Cliente newObjeto = find(objeto.getId());
		updateData(newObjeto, objeto);
		return repository.save(newObjeto);
	}

	public void delete(Integer id) {
		find(id);
		try {
			repository.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possível excluir porque há Entidades Relacionadas");
		}
	}

	public List<Cliente> findAll() {
		return repository.findAll();
	}

	public Page<Cliente> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		return repository.findAll(pageRequest);
	}

	public Cliente fromDTO(ClienteDTO objetoDto) {
		return new Cliente(objetoDto.getId(), objetoDto.getNome(), objetoDto.getEmail(), null, null);
	}

	private void updateData(Cliente newObjeto, Cliente objeto) {
		newObjeto.setNome(objeto.getNome());
		newObjeto.setEmail(objeto.getEmail());
	}

}
