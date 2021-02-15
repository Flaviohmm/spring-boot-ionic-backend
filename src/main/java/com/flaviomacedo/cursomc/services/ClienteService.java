package com.flaviomacedo.cursomc.services;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.flaviomacedo.cursomc.domain.Cidade;
import com.flaviomacedo.cursomc.domain.Cliente;
import com.flaviomacedo.cursomc.domain.Endereco;
import com.flaviomacedo.cursomc.domain.enums.Perfil;
import com.flaviomacedo.cursomc.domain.enums.TipoCliente;
import com.flaviomacedo.cursomc.dto.ClienteDTO;
import com.flaviomacedo.cursomc.dto.ClienteNewDTO;
import com.flaviomacedo.cursomc.repositories.ClienteRepository;
import com.flaviomacedo.cursomc.repositories.EnderecoRepository;
import com.flaviomacedo.cursomc.security.UserSS;
import com.flaviomacedo.cursomc.services.exceptions.AuthorizationException;
import com.flaviomacedo.cursomc.services.exceptions.DataIntegrityException;
import com.flaviomacedo.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class ClienteService {

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private ClienteRepository repository;

	@Autowired
	private EnderecoRepository enderecoRepository;
	
	@Autowired
	private S3Service s3Service;

	public Cliente find(Integer id) {
		
		UserSS user = UserService.authenticated();
		if (user == null || !user.hasRole(Perfil.ADMIN) && !id.equals(user.getId())) {
			throw new AuthorizationException("Acesso Negado");
		}
		
		Optional<Cliente> obj = repository.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Cliente.class.getName()));
	}

	@Transactional
	public Cliente insert(Cliente objeto) {
		objeto.setId(null);
		objeto = repository.save(objeto);
		enderecoRepository.saveAll(objeto.getEnderecos());
		return objeto;
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
			throw new DataIntegrityException("Não é possível excluir porque há Pedidos Relacionadas");
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
		return new Cliente(objetoDto.getId(), objetoDto.getNome(), objetoDto.getEmail(), null, null, null);
	}

	public Cliente fromDTO(ClienteNewDTO objetoDto) {
		Cliente cliente = new Cliente(null, objetoDto.getNome(), objetoDto.getEmail(), objetoDto.getCpfOuCnpj(),
				TipoCliente.toEnum(objetoDto.getTipo()), passwordEncoder.encode(objetoDto.getSenha()));
		Cidade cidade = new Cidade(objetoDto.getCidadeId(), null, null);
		Endereco endereco = new Endereco(null, objetoDto.getLogradouro(), objetoDto.getNumero(),
				objetoDto.getComplemento(), objetoDto.getBairro(), objetoDto.getCep(), cliente, cidade);
		cliente.getEnderecos().add(endereco);
		cliente.getTelefones().add(objetoDto.getTelefone1());
		if (objetoDto.getTelefone2() != null) {
			cliente.getTelefones().add(objetoDto.getTelefone2());
		}
		if (objetoDto.getTelefone3() != null) {
			cliente.getTelefones().add(objetoDto.getTelefone3());
		}
		return cliente;
	}

	private void updateData(Cliente newObjeto, Cliente objeto) {
		newObjeto.setNome(objeto.getNome());
		newObjeto.setEmail(objeto.getEmail());
	}
	
	public URI uploadProfilePicture(MultipartFile multipartFile) {
		return s3Service.uploadFile(multipartFile);
	}
}
