package com.bolsaideas.springboot.app.dao;

import org.springframework.data.repository.CrudRepository;

import com.bolsaideas.springboot.app.entyties.Usuario;

public interface IUsuarioDao extends CrudRepository<Usuario,Long>{
	public Usuario findByUserName(String username);
}
