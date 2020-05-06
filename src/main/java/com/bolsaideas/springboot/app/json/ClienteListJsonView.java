package com.bolsaideas.springboot.app.json;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import com.bolsaideas.springboot.app.entyties.Cliente;

/**
@author Jose Rodolfo Juarez
@version 1.0
@since 
*/

@Controller("listar.json")
public class ClienteListJsonView extends MappingJackson2JsonView{
	
@Override
protected Object filterModel(Map<String, Object> model) {
	model.remove("titulo");
	model.remove("page");
	@SuppressWarnings("unchecked")
	Page<Cliente> clientes=(Page<Cliente>) model.get("clientes");
	model.remove("clientes");
	model.put("clientes", clientes.getContent());
	return super.filterModel(model);
}
}
