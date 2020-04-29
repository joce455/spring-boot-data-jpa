package com.bolsaideas.springboot.app.service;

import org.springframework.web.multipart.MultipartFile;

public interface IManagerFileService {
public void eliminar(String fileName);
public String putFile(MultipartFile foto);
public void deleteAll();
public void init();
}
