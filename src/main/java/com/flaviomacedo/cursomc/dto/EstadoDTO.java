package com.flaviomacedo.cursomc.dto;

import java.io.Serializable;

import com.flaviomacedo.cursomc.domain.Estado;

public class EstadoDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private String nome;
	
	public EstadoDTO() {
	}
	
	public EstadoDTO(Estado objeto) {
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
