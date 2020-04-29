package com.bolsaideas.springboot.app.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.bolsaideas.springboot.app.entyties.Cliente;
import com.bolsaideas.springboot.app.entyties.Factura;
import com.bolsaideas.springboot.app.entyties.Producto;

public interface IClienteService {
	 public List<Cliente> findAll();
	 public Page<Cliente> findAll(Pageable pageable);
	 public void save(Cliente cliente);
	 public Cliente findOne(Long id);
	 public void deleteOne(Long id);
	 public List<Producto> findByNombre(String nombre);
	 public void saveFactura(Factura factura);
	 public Producto FindProductoById(Long id);
	 public Factura FindFacturaById(Long id);
	 public void deleteFactura(Long id);
	 public Factura fetchFacturaByIdWithClienteWithItemFacturaWithProducto(Long id);
	 public Cliente fetchByIdWithFacturas(Long id);
	 public Page<Producto> findAllProductos(Pageable pageable);
	 public void save(Producto producto);
	 public void deleteProducto(Long id);
}
