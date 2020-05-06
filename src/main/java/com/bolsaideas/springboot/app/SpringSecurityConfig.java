package com.bolsaideas.springboot.app;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


import com.bolsaideas.springboot.app.auth.handler.LoginSuccessHandler;
import com.bolsaideas.springboot.app.service.JpaUserDetailsService;

@EnableGlobalMethodSecurity(securedEnabled = true)
@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
	
	
	@Autowired
	private JpaUserDetailsService userDetailsService;
	
	@Autowired
	LoginSuccessHandler loginSuccessHandler;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/","/locale","/css/**","/js/**","/api/**","/images/**","/listar**").permitAll()
		//.antMatchers("/ver/**").hasAnyRole("USER")
		//.antMatchers("/uploads/**").hasAnyRole("USER")
		//.antMatchers("/form/**").hasAnyRole("ADMIN")
		.antMatchers("/productos/**").hasAnyRole("USER")
		//.antMatchers("/factura/**").hasAnyRole("ADMIN")
		//.antMatchers("/eliminar/**").hasAnyRole("ADMIN")
		.anyRequest().authenticated()
		.and()
		.formLogin()
		.successHandler(loginSuccessHandler)
		.loginPage("/login").permitAll()
		.and()
		.logout().permitAll()
		.and()
		.exceptionHandling().accessDeniedPage("/error_403");
	}

	
	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder builder) {
	
		
		try {
			builder.userDetailsService(userDetailsService)	
			.passwordEncoder(passwordEncoder);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*	PasswordEncoder encoder= passwordEncoder;
		UserBuilder users= User.builder().passwordEncoder(encoder::encode);
	
	 try {
		 builder.inMemoryAuthentication()
		 .withUser(users.username("admin").password("1234").roles("ADMIN","USER"))
		 .withUser(users.username("jose").password("1234").roles("USER"));
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}*/
	}
	
}
