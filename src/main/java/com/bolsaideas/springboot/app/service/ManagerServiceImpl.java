package com.bolsaideas.springboot.app.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;



@Service
public class ManagerServiceImpl implements IManagerFileService {

	@Override
	public void eliminar(String fileName) {
		Path pathFoto= Paths.get("uploads").resolve(fileName).toAbsolutePath();
		File archivo=pathFoto.toFile();
		if (archivo.exists() && archivo.canRead()) {
			archivo.delete();
		}

	}

	@Override
	public String putFile(MultipartFile foto) {
		
		String uniqueFilename= UUID.randomUUID().toString()+"_"+foto.getOriginalFilename();
		
		Path rootPath= Paths.get("uploads").resolve(uniqueFilename);
		Path rootAdsolutePath= rootPath.toAbsolutePath();
		
		try {
			
			Files.copy(foto.getInputStream(), rootAdsolutePath);
			return uniqueFilename;
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		return "";

	}

	@Override
	public void deleteAll() {
		FileSystemUtils.deleteRecursively( Paths.get("uploads").toFile());
		
	}

	@Override
	public void init() {
		try {
			Files.createDirectory( Paths.get("uploads"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
