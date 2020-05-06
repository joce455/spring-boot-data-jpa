package com.bolsaideas.springboot.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.bolsaideas.springboot.app.service.IManagerFileService;

@SpringBootApplication
public class SpringBootDataJpaApplication implements CommandLineRunner{

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	IManagerFileService managerFileService;
	public static void main(String[] args) {
		SpringApplication.run(SpringBootDataJpaApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		managerFileService.deleteAll();
		managerFileService.init();
		
		String password="1234";
		
		for (int i = 0; i < 2; i++) {
			String bcrypPassword= passwordEncoder.encode(password);
			System.out.println(bcrypPassword);
		}
		
	}

}
