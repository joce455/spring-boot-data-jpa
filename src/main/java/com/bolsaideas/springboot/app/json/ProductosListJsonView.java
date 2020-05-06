package com.bolsaideas.springboot.app.json;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;


import com.bolsaideas.springboot.app.entyties.Producto;

/**
@author Jose Rodolfo Juarez
@version 1.0
@since 
*/

@Controller("producto/listar-productos.json")
public class ProductosListJsonView extends MappingJackson2JsonView{
	
@Override
protected Object filterModel(Map<String, Object> model) {
	model.remove("titulo");
	model.remove("page");
	@SuppressWarnings("unchecked")
	Page<Producto> productos=(Page<Producto>) model.get("productos");
	model.remove("productos");
	model.put("productos", productos.getContent());
	return super.filterModel(model);
}
}