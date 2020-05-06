package com.bolsaideas.springboot.app.controllers;


import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bolsaideas.springboot.app.entyties.Cliente;
import com.bolsaideas.springboot.app.service.IClienteService;
import com.bolsaideas.springboot.app.service.IManagerFileService;
import com.bolsaideas.springboot.app.utils.PageRender;


@Controller
@SessionAttributes("cliente")
public class ClienteController {

	@Autowired
	private IManagerFileService managerFileService;
	@Autowired
	@Qualifier("clienteService")
	private IClienteService clienteService;

	@Autowired
	private MessageSource messageSource;
	

	
	
	@GetMapping({"/listar","/"})
	public String listar(@RequestParam(name = "page", defaultValue = "0") int page, Model model,
			Locale locale) {
		Pageable pageRequest = PageRequest.of(page, 4);
		Page<Cliente> clientes = clienteService.findAll(pageRequest);

		PageRender<Cliente> pageRender = new PageRender<>("/listar", clientes);
		
		model.addAttribute("titulo", messageSource.getMessage("text.cliente.listar.titulo", null, locale));
		model.addAttribute("clientes", clientes);
		model.addAttribute("page", pageRender);
		return "listar";
	}
	/*private boolean hasRole(String rol) {
		SecurityContext context= SecurityContextHolder.getContext();
		if (context==null) {
			return false;
		}
		Authentication auth= context.getAuthentication();
		if (auth==null) {
			return false;
		}
		
		Collection<? extends GrantedAuthority> authorities=auth.getAuthorities();
		
		for (GrantedAuthority authority:authorities) {
			if (rol.equals(authority.getAuthority())) {
				return true;
			}
		}
		return false;
	}*/
	@Secured("ROLE_ADMIN")
	@GetMapping("/form")
	public String crear(Model model,Locale locale) {
		model.addAttribute("titulo", "Crear cliente");
		Cliente cliente = new Cliente();
		model.addAttribute("titulo", messageSource.getMessage("text.cliente.form.titulo.crear", null, locale));
		model.addAttribute("buttonText", messageSource.getMessage("text.cliente.crear", null, locale));
		model.addAttribute("cliente", cliente);
		return "form";
	}

	@Secured("ROLE_ADMIN")
	@PostMapping("/form")
	public String procesar(@Valid Cliente cliente, BindingResult result, Model model,
			@RequestParam(name = "file") MultipartFile foto, RedirectAttributes flash, SessionStatus status,Locale locale) {

		if (result.hasErrors()) {
			messageSource.getMessage("text.cliente.form.titulo.crear", null, locale);
			
			
			return "form";
		}

		if (!foto.isEmpty()) {
			if (cliente.getId() > 0 && cliente.getFoto() != null && cliente.getFoto().length() > 0) {
				managerFileService.eliminar(cliente.getFoto());
			}

			cliente.setFoto(managerFileService.putFile(foto));
			flash.addFlashAttribute("info",
					"La foto " + foto.getOriginalFilename() + " se ha subido de forma correcta");
		}

		if(cliente.getId()!=0) {
			flash.addFlashAttribute("success", messageSource.getMessage("text.cliente.flash.editar.success", null, locale));
		}else {
			flash.addFlashAttribute("success", messageSource.getMessage("text.cliente.flash.crear.success", null, locale));
		}
		
		clienteService.save(cliente);
		status.setComplete();
	
		return "redirect:listar";
	}

	@Secured("ROLE_ADMIN")
	@GetMapping("/form/{id}")
	public String editar(@PathVariable Long id, Model model, RedirectAttributes flash,Locale locale) {
		Cliente cliente = null;
		if (id > 0) {
			cliente = clienteService.findOne(id);
			if (cliente == null) {
				flash.addFlashAttribute("danger", messageSource.getMessage("text.cliente.flash.db.error", null, locale));
				
				return "redirect:/listar";
			}
		} else {
			
			flash.addFlashAttribute("danger", messageSource.getMessage("text.cliente.flash.id.error", null, locale));
			return "redirect:/listar";
		}
		model.addAttribute("titulo", messageSource.getMessage("text.cliente.form.titulo.editar", null, locale));
		model.addAttribute("buttonText", messageSource.getMessage("text.cliente.editar", null, locale));
		model.addAttribute("cliente", cliente);

		return "form";
	}

	@Secured("ROLE_ADMIN")
	@GetMapping("/borrar/{id}")
	public String borrar(@PathVariable Long id, Model model, RedirectAttributes flash,Locale locale) {
		Cliente cliente = null;

		if (id > 0) {
			cliente = clienteService.findOne(id);
			if (cliente != null) {
				clienteService.deleteOne(id);
				
				
				flash.addFlashAttribute("success",messageSource.getMessage("text.cliente.flash.eliminar.success", null, locale) );
				managerFileService.eliminar(cliente.getFoto());

			} else {
				
				flash.addFlashAttribute("danger", messageSource.getMessage("text.cliente.flash.db.error", null, locale));
			}
		}

		return "redirect:/listar";

	}

	@Secured("ROLE_USER")
	@GetMapping("/ver/{id}")
	public String ver(@PathVariable Long id, Model model, RedirectAttributes flash,Locale locale) {
		Cliente cliente = null;
		if (id > 0) {
			//cliente = clienteService.findOne(id);
			cliente = clienteService.fetchByIdWithFacturas(id);
			if (cliente == null) {
			
				flash.addFlashAttribute("danger", messageSource.getMessage("text.cliente.flash.db.error", null, locale));
				
				return "redirect:/listar";
			}

		}
		model.addAttribute("titulo", messageSource.getMessage("text.cliente.detalle.titulo", null, locale));
		model.addAttribute("cliente", cliente);
		return "ver";

	}

	@Secured("ROLE_USER")
	@GetMapping("/uploads/{filename:.+}")
	public ResponseEntity<Resource> verFoto(@PathVariable String filename) {
		Path pathFoto = Paths.get("uploads").resolve(filename).toAbsolutePath();
		Resource recurso = null;
		try {
			recurso = new UrlResource(pathFoto.toUri());
			if (!recurso.exists() && !recurso.isReadable()) {
				throw new RuntimeException("No se puede cargar imagen " + pathFoto.toString());
			}
		} catch (MalformedURLException e) {

			e.printStackTrace();
		}

		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + recurso.getFilename() + "\"")
				.body(recurso);
	}

}
