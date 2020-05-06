package com.bolsaideas.springboot.app.controllers;

import java.util.Locale;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
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
import com.bolsaideas.springboot.app.entyties.Producto;
import com.bolsaideas.springboot.app.service.IClienteService;
import com.bolsaideas.springboot.app.utils.PageRender;

@Controller
@RequestMapping("/productos")
@SessionAttributes("producto")
public class ProductosController {
	@Autowired
	IClienteService clienteService;
	
	@Autowired
	private MessageSource messageSource;
	
	@GetMapping("/listar-productos")
	public String listar(@RequestParam(name = "page", defaultValue = "0") int page, Model model,Locale locale) {
		Pageable pageRequest = PageRequest.of(page, 4);
		Page<Producto> productos = clienteService.findAllProductos(pageRequest);

		PageRender<Producto> pageRender = new PageRender<>("/productos/listar-productos", productos);
		
		model.addAttribute("titulo", messageSource.getMessage("text.producto.lista.titulo", null, locale));
		
		model.addAttribute("productos", productos);
		model.addAttribute("page", pageRender);
		return "producto/listar-productos";
	}
	
	@GetMapping("/form")
	public String crear(Model model,Locale locale) {
		model.addAttribute("buttonText", messageSource.getMessage("text.producto.boton.crear", null, locale));
		model.addAttribute("titulo", messageSource.getMessage("text.producto.boton.crear", null, locale));
		Producto producto = new Producto();
		model.addAttribute("producto", producto);
		return "producto/form";
	}
	
	@PostMapping("/form")
	public String procesar(@Valid Producto producto, BindingResult result, Model model,
			RedirectAttributes flash, SessionStatus status,Locale locale) {

		if (result.hasErrors()) {
			model.addAttribute("titulo", messageSource.getMessage("text.producto.boton.crear", null, locale));
			return "productos/form";
		}
		
		if(producto.getId()!=null) {
			flash.addFlashAttribute("success", messageSource.getMessage("text.producto.flash.editar.success", null, locale));
		}else {
			flash.addFlashAttribute("success", messageSource.getMessage("text.producto.flash.crear.success", null, locale));
		}
	
		clienteService.save(producto);
		status.setComplete();
		
		return "redirect:/productos/listar-productos";
	}
	@GetMapping("/form/{id}")
	public String editar(@PathVariable Long id, Model model, RedirectAttributes flash,Locale locale) {
		Producto producto = null;
		if (id > 0) {
			producto = clienteService.FindProductoById(id);
			if (producto == null) {
				flash.addFlashAttribute("danger", messageSource.getMessage("text.producto.flash.db.error", null, locale));
				return "redirect:/producto/listar-productos";
			}
		} else {
			flash.addFlashAttribute("danger", messageSource.getMessage("text.producto.flash.id.error", null, locale));
			return "redirect:/producto/listar-productos";
		}
		
		model.addAttribute("titulo", messageSource.getMessage("text.producto.editar.titulo", null, locale));
		model.addAttribute("buttonText", messageSource.getMessage("text.cliente.editar", null, locale));
		model.addAttribute("producto", producto);

		return "/producto/form";
	}

	@GetMapping("/borrar/{id}")
	public String borrar(@PathVariable Long id, Model model, RedirectAttributes flash,Locale locale) {
		Producto producto = null;

		if (id > 0) {
			producto = clienteService.FindProductoById(id);
			if (producto != null) {
				clienteService.deleteProducto(id);
				flash.addFlashAttribute("success",messageSource.getMessage("text.producto.flash.eliminar.success", null, locale) );

			} else {
				flash.addFlashAttribute("danger", messageSource.getMessage("text.producto.flash.db.error", null, locale));
			}
		}

		return "redirect:/productos/listar-productos";

	}
}
