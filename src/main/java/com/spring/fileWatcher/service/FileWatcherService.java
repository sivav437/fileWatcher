package com.spring.fileWatcher.service;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
//import jakarta.persistence.criteria.Path;

@Service
public class FileWatcherService {
	
	@Autowired
	FileProcessor processor;
	
	@PostConstruct
    public void startWatching() throws IOException {
		
		WatchService watchService = FileSystems.getDefault().newWatchService();
        Path path = Paths.get("/home/ctuser/ExcelFiles/");
        path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);
        
        Executors.newSingleThreadExecutor().execute(() -> {
//            while (true) {
                try {
                    WatchKey key = watchService.take();
                    for (WatchEvent<?> event : key.pollEvents()) {
                    	
                        Path filePath = (Path) event.context();
                        
                        if (filePath.toString().endsWith(".xlsx")) { 
                        	processor.processFile(filePath);
                            System.out.println(filePath+" : filePath");
                        }
                    }
                    key.reset();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    //break;
                }
//            }
 catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        });
	}
	


}
