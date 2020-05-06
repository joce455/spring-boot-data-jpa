package com.bolsaideas.springboot.app.controllers;
/**
@author Jose Rodolfo Juarez
@version 1.0
@since 
*/

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bolsaideas.springboot.app.entyties.Cliente;
import com.bolsaideas.springboot.app.service.IClienteService;

@RestController
@RequestMapping("/api")
public class ClienteRestController {

	@Autowired
	@Qualifier("clienteService")
	private IClienteService clienteService;
	
	@GetMapping("/listar")
	public List<Cliente> listar() {
		List<Cliente> lista=clienteService.findAll();
		return lista;


	}
}
