package com.sa.sbsimplewebapp.storageservice;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.security.core.Authentication;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.sa.sbsimplewebapp.model.domain.User;
import com.sa.sbsimplewebapp.service.UserService;

public class LocalStorageService implements StorageService {
//	private static final Logger logger = LoggerFactory.getLogger(LocalStorageService.class);
	private Path publicStoragePath;
	private Path privateStoragePath;
	
	@Autowired
	private UserService userService;

	public LocalStorageService(String publicStoragePath, String privateStoragePath) {
		this.publicStoragePath = Paths.get(publicStoragePath);
		this.privateStoragePath = Paths.get(privateStoragePath);
		
		init();
	}
	
	
//
//	public Path getPublicStoragePath() {
//		return publicStoragePath;
//	}
//	public void setPublicStoragePath(String publicStoragePath) {
//		this.publicStoragePath = Paths.get(publicStoragePath);
//	}
//	public Path getPrivateStoragePath() {
//		return privateStoragePath;
//	}
//	public void setPrivateStoragePath(String privateStoragePath) {
//		this.privateStoragePath = Paths.get(privateStoragePath);
//	}

	@Override
	public void init() {
		try {
			Files.createDirectories(publicStoragePath);
			Files.createDirectories(privateStoragePath);
		} catch (IOException e) {
			throw new StorageException("Could not initialize storage", e);
		}
	}

	@Override
	public void store(MultipartFile file, Authentication authentication) {
		String filename = StringUtils.cleanPath(file.getOriginalFilename());
		try {
			if (file.isEmpty()) {
				throw new StorageException("Failed to store empty file " + filename);
			}
			if (filename.contains("..")) {
				// This is a security check
				throw new StorageException("Cannot store file with relative path outside current directory " + filename);
			}
			if (authentication != null) {
				User user = userService.findByUsername(authentication.getName());
				
				if (user == null) {
					throw new StorageException("Cannot find user with username " + authentication.getName());
				}
				Path userStoragePath = privateStoragePath.resolve(Paths.get(String.valueOf(user.getId())));
				if (!Files.exists(userStoragePath)) {
					Files.createDirectories(userStoragePath);
				}
				
				InputStream inputStream = file.getInputStream();
				Files.copy(inputStream, userStoragePath.resolve(filename), StandardCopyOption.REPLACE_EXISTING);
			}
			else {
				InputStream inputStream = file.getInputStream();
				Files.copy(inputStream, publicStoragePath.resolve(filename), StandardCopyOption.REPLACE_EXISTING);
			}
		} catch (IOException e) {
			throw new StorageException("Failed to store file " + filename, e);
		}
	}

	@Override
	public Path load(String filename, Authentication authentication) {
		if (authentication != null) {
			User user = userService.findByUsername(authentication.getName());
			if (user == null) {
				throw new StorageException("Cannot find user with username " + authentication.getName());
			}
			Path userStoragePath = privateStoragePath.resolve(Paths.get(String.valueOf(user.getId())));
			return userStoragePath.resolve(filename);
		}
		return publicStoragePath.resolve(filename);
	}

	@Override
	public Resource loadAsResource(String filename, Authentication authentication) {
		try {
			Path file = load(filename, authentication);
			Resource resource = new UrlResource(file.toUri());
			if (resource.exists() && resource.isReadable()) {
				return resource;
			} else {
				throw new StorageException("Could not read file: " + filename);

			}
		} catch (MalformedURLException e) {
			throw new StorageException("Could not read file: " + filename, e);
		}
	}


	@Override
	public List<Path> listPublicFiles() {
		return listFiles(publicStoragePath);
	}


	@Override
	public List<Path> listPrivateFiles(Authentication authentication) {
		if (authentication != null) {
			User user = userService.findByUsername(authentication.getName());
			if (user == null) {
				throw new StorageException("Cannot find user with username " + authentication.getName());
			}
			Path userStoragePath = privateStoragePath.resolve(Paths.get(String.valueOf(user.getId())));
			
			if (!Files.exists(userStoragePath)) {
				try {
					Files.createDirectories(userStoragePath);
				} catch (IOException e1) {
					throw new StorageException("Could not create directory");
				}
			}
			
			return listFiles(userStoragePath);
		}
		throw new StorageException("Could not find user");
	}
	
	protected List<Path> listFiles(Path directory) {
		List<Path> files = new ArrayList<>();
		
		try {
			Files.walk(directory, 1).filter(path -> !path.equals(directory)).forEach(file -> files.add(file));
			
			return files;
		} catch (IOException | UncheckedIOException e) {
			throw new StorageException("Could not explore directory", e);
		}
	}

	/*
	@Override
	public void deleteAll() {
		FileSystemUtils.deleteRecursively(rootLocation.toFile());
	}
	*/
}
