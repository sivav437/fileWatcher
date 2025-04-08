package com.spring.fileWatcher.service;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
//import jakarta.persistence.criteria.Path;

@Service
public class FileWatcherService {
	
	@Autowired
	FileProcessor processor;
	
	private ExecutorService executorService ;

	
	private static final Logger log = LogManager.getLogger(FileWatcherService.class);
	
	// registers a watcher service to a basePath.
	private WatchService createWatchService(String basePath) throws IOException {
		
		WatchService watchService = FileSystems.getDefault().newWatchService();
        Path path = Paths.get(basePath);
        path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE); // for create events only
		return watchService;
		 
		
	}
	
	
	
	
	private void findEventsAndProcess(WatchService watchService,String basePath,ExecutorService executorService)  {
		try {

		WatchKey key = watchService.take();
		for (WatchEvent<?> event : key.pollEvents()) {
        	
			if(event.kind()==StandardWatchEventKinds.ENTRY_CREATE) {
				Path filePath = (Path) event.context();
				
	            
	            if (filePath.toString().endsWith(".xlsx")) { 
	            	
	            	Path fullPath = Paths.get(basePath, filePath.toString());
	            	
	            	 // 🔥 Submit file processing to thread pool
	            	 executorService.submit(() -> {
                         try {
                        	 
                        	 log.info("Thread " + Thread.currentThread().getName() + " is processing file: " + fullPath);
                             processor.processFile(fullPath);
                             
                         } catch (Exception e) {
                             e.printStackTrace(); // or log
                         }
                     });
	            }
			}
            
        }
        key.reset();
		}catch(Exception ee) {
			ee.printStackTrace();
		}
		
	}
	

	
	@PostConstruct
    public void startWatching() throws IOException {
		
		// base path of excel files to store.
		String basePath="/home/ctuser/ExcelFiles";
		
		WatchService watchService=this.createWatchService(basePath);
		
		int numberOfThreads = Runtime.getRuntime().availableProcessors(); // total threads available.
		this.executorService = Executors.newFixedThreadPool(numberOfThreads); // fixed thread.
		
		Executors.newSingleThreadExecutor().submit(() -> { // spans a background thread using executors.
			
			// Continuously polls for file creation events and handles them in the thread.
	        while (true) {
	        	
	        	// Good for continuous checking without stalling. ( pool => non-blocking IO)
	        	this.findEventsAndProcess(watchService,basePath,this.executorService);
	        	
	        }
		});
		
		
		
	}
	
	
	@PreDestroy
	public void shutdown() {
	    executorService.shutdown();
	    try {
	        if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
	            executorService.shutdownNow();
	        }
	    } catch (InterruptedException e) {
	        executorService.shutdownNow();
	    }
	}
	
	
	


}



