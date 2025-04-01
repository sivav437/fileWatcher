package com.spring.fileWatcher.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.spring.fileWatcher.model.FileStatus;
import com.spring.fileWatcher.repository.FileStatusRepository;

@Service
public class FileStatusService {
	
	@Autowired
    private FileStatusRepository statusRepository;
	
	
    public FileStatus getFileStatus(String fileId) {
    	
    	Optional<FileStatus>  status=statusRepository.findById(fileId);
    	if(status.isPresent()) {
    		switch(status.get().getStatus()) {
    			case "PENDING":
    				System.out.println("status is pending");
    				break;
    			case "PROCESSING":
    				System.out.println("status is processing");
    				break;
    			case "COMPLETED":
    				System.out.println("status is completed");
    				break;
    			case "FAILED":
    				System.out.println("status is failed");
    				break;
    		}
    		return status.get();
    	}
    	return null;
        
    }
    
    public FileStatus updateStatus(String fileId,String update_status) {
    	FileStatus status_recieved=this.getFileStatus(fileId);
    	if(status_recieved!=null) {
    		status_recieved.setStatus(update_status);
    		return status_recieved;
    	}
    	return null;
    }
    
    public FileStatus createStatus(FileStatus status)  {
    	if(status != null) {
    		return statusRepository.save(status);
    		
    	}
    	//throw new Exception("Unable to create a FileStatus due to null input.");
    	return null;
    }
    
    

}
