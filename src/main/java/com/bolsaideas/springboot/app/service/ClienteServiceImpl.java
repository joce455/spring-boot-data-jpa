package com.bolsaideas.springboot.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bolsaideas.springboot.app.dao.IClienteDao;
import com.bolsaideas.springboot.app.dao.IFacturaDao;
import com.bolsaideas.springboot.app.dao.IProductoDao;
import com.bolsaideas.springboot.app.entyties.Cliente;
import com.bolsaideas.springboot.app.entyties.Factura;
import com.bolsaideas.springboot.app.entyties.Producto;

@Service("clienteService")
public class ClienteServiceImpl implements IClienteService {

	@Autowired
	private IClienteDao clienteDao;
	
	@Autowired
	private IFacturaDao facturaDao;
	@Autowired
	private IProductoDao productoDao;
	@Override
	@Transactional(readOnly = true)
	public List<Cliente> findAll() {
		return (List<Cliente>) clienteDao.findAll();
	}

	@Override
	@Transactional
	public void save(Cliente cliente) {
		clienteDao.save(cliente);
	}

	@Override
	@Transactional(readOnly = true)
	public Cliente findOne(Long id) {
		
		return clienteDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public void deleteOne(Long id) {
		 clienteDao.deleteById(id);
		
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Cliente> findAll(Pageable pageable) {
		
		return clienteDao.findAll(pageable);
	}

	@Override
	public List<Producto> findByNombre(String nombre) {
		
		return productoDao.findByNombreLikeIgnoreCase("%"+nombre+"%");
	}

	@Override
	@Transactional
	public void saveFactura(Factura factura) {
		facturaDao.save(factura);
	}

	@Override
	@Transactional(readOnly = true)
	public Producto FindProductoById(Long id) {
		return productoDao.findById(id).orElse(null);
	}

	@Override
	@Transactional(readOnly = true)
	public Factura FindFacturaById(Long id) {
		return facturaDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public void deleteFactura(Long id) {
		facturaDao.deleteById(id);
		
	}

	@Override
	@Transactional(readOnly = true)
	public Factura fetchFacturaByIdWithClienteWithItemFacturaWithProducto(Long id) {
		return facturaDao.fetchByIdWithClienteWithItemFacturaWithProducto(id);
		
	}

	@Override
	@Transactional(readOnly = true)
	public Cliente fetchByIdWithFacturas(Long id) {
		return clienteDao.fetchByIdWithFacturas(id);
	}

	@Override
	public Page<Producto> findAllProductos(Pageable pageable) {
		return productoDao.findAll(pageable);
		
	}

	@Override
	@Transactional
	public void save(Producto producto) {
		productoDao.save(producto);
	}

	@Override
	@Transactional
	public void deleteProducto(Long id) {
		productoDao.deleteById(id);
		
	}

}
