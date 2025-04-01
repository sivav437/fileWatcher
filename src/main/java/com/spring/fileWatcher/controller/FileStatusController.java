package com.spring.fileWatcher.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.fileWatcher.model.FileStatus;
import com.spring.fileWatcher.repository.FileStatusRepository;
import com.spring.fileWatcher.service.FileStatusService;

@RestController
@RequestMapping("/api/file")
public class FileStatusController {
	
	@Autowired
    private FileStatusRepository statusRepository;
	
	@Autowired
	FileStatusService statusService;
	
	@GetMapping("/status/{fileId}")
    public ResponseEntity<FileStatus> getFileStatus(@PathVariable String fileId) {
		
		FileStatus status_rcvd =statusService.getFileStatus(fileId);
		if(status_rcvd==null) {
			return new ResponseEntity<>(null,HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(status_rcvd,HttpStatus.OK);

    }

}
