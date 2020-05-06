package com.bolsaideas.springboot.app.controllers;

import java.util.List;
import java.util.Locale;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bolsaideas.springboot.app.entyties.Cliente;
import com.bolsaideas.springboot.app.entyties.Factura;
import com.bolsaideas.springboot.app.entyties.ItemFactura;
import com.bolsaideas.springboot.app.entyties.Producto;
import com.bolsaideas.springboot.app.service.IClienteService;


@Secured("ROLE_ADMIN")
@Controller
@RequestMapping("/factura")
@SessionAttributes("factura")
public class FacturaController {

	@Autowired
	IClienteService clienteService;
	
	@Autowired
	private MessageSource messageSource;
	
	@GetMapping("/form/{clienteId}")
	public String crear(@PathVariable Long clienteId, Model model, RedirectAttributes flash,Locale locale) {
		Cliente resultado=clienteService.findOne(clienteId);
		
		if (resultado==null) {
			flash.addFlashAttribute("danger", "el cliente no existe en la db");
			return "redirect:/listar";
		}
		Factura factura= new  Factura();
		factura.setCliente(resultado);
		
		model.addAttribute("factura", factura);
		model.addAttribute("titulo", messageSource.getMessage("text.cliente.factura.crear", null, locale));
		
		return "factura/form";
	}
	@GetMapping("/ver/{id}")
	public String verDetalleFactura(
			@PathVariable Long id, Model model, RedirectAttributes flash,Locale locale) {
		
		//Factura factura= clienteService.FindFacturaById(id);
		Factura factura=clienteService.fetchFacturaByIdWithClienteWithItemFacturaWithProducto(id);
		if (factura==null) {
			flash.addFlashAttribute("danger", "la factura no existe en la db");
			return "redirect:/listar";
		}
		
		model.addAttribute("factura",factura);
		model.addAttribute("titulo", messageSource.getMessage("text.cliente.factura.detalle", null, locale));
		
		return "factura/ver";
	}
	
	//pone la respuesta dentro del response body
	@GetMapping(value="/cargar-productos/{term}",produces= {"application/json"})
	public @ResponseBody List<Producto> cargarProductos(@PathVariable String term) {
		return clienteService.findByNombre(term);
	}
	
	@PostMapping("/form")
	public String guardarFactura(@Valid Factura factura,
			BindingResult result,
			Model model,
			RedirectAttributes flash,
			@RequestParam(name="item_id[]",required=false) Long[] itemId,
			@RequestParam(name="cantidad[]",required=false) Integer[] cantidad,
			SessionStatus status) {
		
		if (result.hasErrors()) {
			model.addAttribute("titulo","Crear factura");
			return "factura/form";
		}
		if(itemId ==null || itemId.length == 0) {
			model.addAttribute("titulo","Crear factura");
			model.addAttribute("danger", "Debe incluir al menos un item!!");
			return "factura/form";
		}
		for (int i = 0; i < itemId.length; i++) {
			Producto producto=clienteService.FindProductoById(itemId[i]);
			ItemFactura linea= new ItemFactura();
			linea.setCantidad(cantidad[i]);
			linea.setProducto(producto);
			factura.addItems(linea);
		}
		
		clienteService.saveFactura(factura);
		status.setComplete();
		flash.addFlashAttribute("success","Factura creada con exito!!");
		return "redirect:/ver/"+factura.getCliente().getId();
	}
	
	
	    @GetMapping("/eliminar/{id}")
		public String eliminar(@PathVariable Long id,RedirectAttributes flash) {
	    	Factura resultado= clienteService.FindFacturaById(id);
	    	
	    	if (resultado!=null) {
	    		clienteService.deleteFactura(id);
	    		flash.addFlashAttribute("success", "factura eliminada con exito");
	    		return "redirect:/ver/"+resultado.getCliente().getId();
			}
	    	
	    	flash.addFlashAttribute("danger", "La factura no existe en la db");
	    	return "redirect:/listar";
		}
	
}
