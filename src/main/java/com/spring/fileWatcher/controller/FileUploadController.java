package com.spring.fileWatcher.controller;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.spring.fileWatcher.model.FileStatus;
import com.spring.fileWatcher.repository.FileStatusRepository;
import com.spring.fileWatcher.service.FileStatusService;

@RestController
@RequestMapping("/api/file")
public class FileUploadController {
	
	
	
	@Autowired
	FileStatusService statusService;
	
	@PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        
		String fileId = UUID.randomUUID().toString();
		String originalName=file.getOriginalFilename();
        String filePath = "/home/ctuser/ExcelFiles/" + fileId + "_" + originalName;
        
        try {
            File dest = new File(filePath);
            FileStatus status=new FileStatus();
            System.out.println(dest.getAbsolutePath()+" abs path");
            
            file.transferTo(dest);
            status.setFileId(fileId);
            status.setOriginalFileName(originalName);
            status.setStatus("PENDING");
            status.setTimestamp(new Date());
            statusService.createStatus(status);

            System.out.println("data is added into status table you can track happen");
            return ResponseEntity.ok(fileId); // Return unique key to client
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
