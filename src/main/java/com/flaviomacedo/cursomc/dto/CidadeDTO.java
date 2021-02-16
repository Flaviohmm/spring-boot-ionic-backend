package com.flaviomacedo.cursomc.dto;

import java.io.Serializable;

import com.flaviomacedo.cursomc.domain.Cidade;

public class CidadeDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private String nome;
	
	public CidadeDTO() {
	}

	public CidadeDTO(Cidade objeto) {
		id = objeto.getId();
		nome = objeto.getNome();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
}
