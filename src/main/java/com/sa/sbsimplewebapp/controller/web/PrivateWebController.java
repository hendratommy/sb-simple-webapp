package com.sa.sbsimplewebapp.controller.web;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sa.sbsimplewebapp.storageservice.StorageService;

@Controller
@RequestMapping("/private")
public class PrivateWebController {
	private static final Logger logger = LoggerFactory.getLogger(PrivateWebController.class);
	
	@Autowired
	private StorageService storageService;
	
	@GetMapping
	public String index(HttpServletRequest request, Authentication authentication, Model uiModel) {
		logger.debug(request.getMethod() + " " + request.getRequestURI()
				+ (request.getQueryString() != null ? "?" + request.getQueryString() : ""));
		
		List<Path> files = new ArrayList<>();
		List<Path> images = new ArrayList<>();
		
		for (Path file : storageService.listPrivateFiles(authentication)) {
			if (FilenameUtils.getExtension(file.getFileName().toString()).matches("(?i:jpg|jpeg|png|gif)"))
				images.add(file);
			else files.add(file);
		}
		
		uiModel.addAttribute("images", images);
		uiModel.addAttribute("files", files);
		
		return "private/private-files";
	}
	
	@GetMapping("/upload")
	public String uploadPage(HttpServletRequest request) {
		logger.debug(request.getMethod() + " " + request.getRequestURI()
				+ (request.getQueryString() != null ? "?" + request.getQueryString() : ""));
		
		return "forward:/private";
	}
	
	@PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes, Authentication authentication, HttpServletRequest request) {
		logger.debug(request.getMethod() + " " + request.getRequestURI()
			+ (request.getQueryString() != null ? "?" + request.getQueryString() : ""));
		
        storageService.store(file, authentication);
        redirectAttributes.addFlashAttribute("message", file.getOriginalFilename() + " uploaded");

        return "redirect:/private";
    }
	
	@GetMapping(value="files/{fileName:.+}", produces=MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<Resource> serveFile(@PathVariable String fileName, Authentication authentication, HttpServletRequest request) {
		logger.debug(request.getMethod() + " " + request.getRequestURI()
			+ (request.getQueryString() != null ? "?" + request.getQueryString() : ""));
		
		Resource file = storageService.loadAsResource(fileName, authentication);
		
		logger.debug("filename: " + file.getFilename());
		
		if (FilenameUtils.getExtension(file.getFilename()).matches("(?i:jpg|jpeg|png|gif)"))
			return ResponseEntity.ok().body(file);
		else return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
	}
}
