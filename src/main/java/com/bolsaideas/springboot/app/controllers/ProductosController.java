package com.bolsaideas.springboot.app.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bolsaideas.springboot.app.entyties.Cliente;
import com.bolsaideas.springboot.app.entyties.Producto;
import com.bolsaideas.springboot.app.service.IClienteService;
import com.bolsaideas.springboot.app.utils.PageRender;

@Controller
@RequestMapping("/productos")
@SessionAttributes("producto")
public class ProductosController {
	@Autowired
	IClienteService clienteService;
	
	@GetMapping("/listar")
	public String listar(@RequestParam(name = "page", defaultValue = "0") int page, Model model) {
		Pageable pageRequest = PageRequest.of(page, 4);
		Page<Producto> productos = clienteService.findAllProductos(pageRequest);

		PageRender<Producto> pageRender = new PageRender<>("/productos/listar", productos);
		model.addAttribute("titulo", "Listado de  productos");
		model.addAttribute("productos", productos);
		model.addAttribute("page", pageRender);
		return "producto/listar";
	}
	
	@GetMapping("/form")
	public String crear(Model model) {
		model.addAttribute("titulo", "Crear producto");
		Producto producto = new Producto();
		model.addAttribute("producto", producto);
		return "producto/form";
	}
	
	@PostMapping("/form")
	public String procesar(@Valid Producto producto, BindingResult result, Model model,
			RedirectAttributes flash, SessionStatus status) {

		if (result.hasErrors()) {
			model.addAttribute("titulo", "Resultado creacion");
			return "productos/form";
		}

		

		clienteService.save(producto);
		status.setComplete();
		flash.addFlashAttribute("success", "Producto creado con exito");
		return "redirect:/productos/listar";
	}
	@GetMapping("/form/{id}")
	public String editar(@PathVariable Long id, Model model, RedirectAttributes flash) {
		Producto producto = null;
		if (id > 0) {
			producto = clienteService.FindProductoById(id);
			if (producto == null) {
				flash.addFlashAttribute("danger", "El id de cliente no se encontro");
				return "redirect:/productos/listar";
			}
		} else {
			flash.addFlashAttribute("danger", "El id de cliente no puede ser cero");
			return "redirect:/productos/listar";
		}
		model.addAttribute("titulo", "Editar producto");
		model.addAttribute("producto", producto);

		return "/producto/form";
	}

	@GetMapping("/borrar/{id}")
	public String borrar(@PathVariable Long id, Model model, RedirectAttributes flash) {
		Producto producto = null;

		if (id > 0) {
			producto = clienteService.FindProductoById(id);
			if (producto != null) {
				clienteService.deleteProducto(id);
				flash.addFlashAttribute("success", "Producto eliminado con exito");

			} else {
				flash.addFlashAttribute("danger", "El id de producto no se encontro");
			}
		}

		return "redirect:/productos/listar";

	}
}
