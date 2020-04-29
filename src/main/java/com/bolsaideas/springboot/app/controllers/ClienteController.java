package com.bolsaideas.springboot.app.controllers;


import java.net.MalformedURLException;


import java.nio.file.Path;
import java.nio.file.Paths;


import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
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

	@GetMapping("/listar")
	public String listar(@RequestParam(name = "page", defaultValue = "0") int page, Model model) {
		Pageable pageRequest = PageRequest.of(page, 4);
		Page<Cliente> clientes = clienteService.findAll(pageRequest);

		PageRender<Cliente> pageRender = new PageRender<>("/listar", clientes);
		model.addAttribute("titulo", "Listado de  clientes");
		model.addAttribute("clientes", clientes);
		model.addAttribute("page", pageRender);
		return "listar";
	}

	@GetMapping("/form")
	public String crear(Model model) {
		model.addAttribute("titulo", "Crear cliente");
		Cliente cliente = new Cliente();
		model.addAttribute("cliente", cliente);
		return "form";
	}

	@PostMapping("/form")
	public String procesar(@Valid Cliente cliente, BindingResult result, Model model,
			@RequestParam(name = "file") MultipartFile foto, RedirectAttributes flash, SessionStatus status) {

		if (result.hasErrors()) {
			model.addAttribute("titulo", "Resultado creacion");
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

		clienteService.save(cliente);
		status.setComplete();
		flash.addFlashAttribute("success", "Cliente creado con exito");
		return "redirect:listar";
	}

	@GetMapping("/form/{id}")
	public String editar(@PathVariable Long id, Model model, RedirectAttributes flash) {
		Cliente cliente = null;
		if (id > 0) {
			cliente = clienteService.findOne(id);
			if (cliente == null) {
				flash.addFlashAttribute("danger", "El id de cliente no se encontro");
				return "redirect:/listar";
			}
		} else {
			flash.addFlashAttribute("danger", "El id de cliente no puede ser cero");
			return "redirect:/listar";
		}
		model.addAttribute("titulo", "Editar cliente");
		model.addAttribute("cliente", cliente);

		return "form";
	}

	@GetMapping("/borrar/{id}")
	public String borrar(@PathVariable Long id, Model model, RedirectAttributes flash) {
		Cliente cliente = null;

		if (id > 0) {
			cliente = clienteService.findOne(id);
			if (cliente != null) {
				clienteService.deleteOne(id);
				flash.addFlashAttribute("success", "Cliente eliminado con exito");

				managerFileService.eliminar(cliente.getFoto());

			} else {
				flash.addFlashAttribute("danger", "El id de cliente no se encontro");
			}
		}

		return "redirect:/listar";

	}

	@GetMapping("/ver/{id}")
	public String ver(@PathVariable Long id, Model model, RedirectAttributes flash) {
		Cliente cliente = null;
		if (id > 0) {
			//cliente = clienteService.findOne(id);
			cliente = clienteService.fetchByIdWithFacturas(id);
			if (cliente == null) {
				flash.addFlashAttribute("danger", "El id de cliente no se encontro");
				return "redirect:/listar";
			}

		}
		model.addAttribute("titulo", "Detalle cliente");
		model.addAttribute("cliente", cliente);
		return "ver";

	}

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
