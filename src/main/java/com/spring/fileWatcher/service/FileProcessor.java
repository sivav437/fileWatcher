package com.spring.fileWatcher.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.stereotype.Service;

@Service
public class FileProcessor {
	
	public void processFile(Path filePath) throws IOException  {
		
		Path targetDir = Paths.get("/home/ctuser/ExcelFiles/mvd/"); 
		
		Files.createDirectories(targetDir);
		
		String fileName = filePath.getFileName().toString();
		
		System.out.println(fileName+" : fileName");
		
		Path targetPath = targetDir.resolve(fileName);
		
		System.out.println(targetPath+" targetPath");
		
		System.out.println(filePath+" filePath");
		
		System.out.println(filePath+" file path at file processor..");
		
		System.out.println(filePath.toAbsolutePath()+" abs path");
		
		//Files.move(filePath.toAbsolutePath(), targetPath, StandardCopyOption.REPLACE_EXISTING);
		System.out.println("File moved from " + filePath + " to " + targetPath);
		
	}

}
