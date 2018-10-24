package com.sa.sbsimplewebapp.storageservice;

import java.nio.file.Path;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

public interface StorageService {
    void init();
    public void store(MultipartFile file, Authentication authentication);
    public Path load(String filename, Authentication authentication);
    public Resource loadAsResource(String filename, Authentication authentication);
    
    public List<Path> listPublicFiles();
    public List<Path> listPrivateFiles(Authentication authentication);
    
//    public void setPublicStoragePath(String publicStoragePath);
//    public Path getPublicStoragePath();
//    
//    public void setPrivateStoragePath(String privateStoragePath);
//    public Path getPrivateStoragePath();
//    public void deleteAll();
}
