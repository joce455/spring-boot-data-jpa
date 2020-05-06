package com.bolsaideas.springboot.app.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bolsaideas.springboot.app.dao.IUsuarioDao;
import com.bolsaideas.springboot.app.entyties.Role;
import com.bolsaideas.springboot.app.entyties.Usuario;


@Service("jpaUserDetailsService")
public class JpaUserDetailsService  implements UserDetailsService{

	@Autowired
	private IUsuarioDao usuarioDao;
	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Usuario usuario= usuarioDao.findByUserName(username);
		 
		List<GrantedAuthority> authorities= new ArrayList<GrantedAuthority>();
		for (Role role: usuario.getRoles()) {
			authorities.add(new SimpleGrantedAuthority(role.getAuthority()));
		}
		
		if (authorities.isEmpty()) {
			throw new UsernameNotFoundException("Error en el login");
		}
		
		return new User(usuario.getUsername(), usuario.getPassword(), usuario.getEnabled(), true, true, true, authorities);
	}

}
