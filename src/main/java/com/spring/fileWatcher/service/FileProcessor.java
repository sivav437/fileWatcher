package com.spring.fileWatcher.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FileProcessor {
	
	@Autowired
	ExcelParserService service;
	
	public void processFile(Path filePath) throws IOException  {
		
		System.out.println(filePath.toAbsolutePath().toString()+" : filePath ABSOLUTE");
		
		String directoryPath = "/home/ctuser/ExcelFiles";
		String fileName=filePath.toString();
		String fileName1 = directoryPath+"/"+fileName;
		try {
		service.parseExcelFile(fileName1);
		
		}catch(Exception ee) {
			System.out.println("Error occured while parsing excel "+ee.getMessage());
		}
		
		int idx=filePath.toString().indexOf(".xlsx");
		
		String str1=filePath.toString().substring(0, idx);
		
		String newFilePath = str1 + ".txt";
		
		//System.out.println("Running as user: " + System.getProperty("user.name"));
		
		
		try {
			
//			String directoryPath = "/home/ctuser/ExcelFiles"; // hardcoded
//			String fileName=filePath.toString();
			
			File directory = new File(directoryPath);
			File sourceFile = new File(directory, fileName);
			
			String targetPath = directoryPath+"/"+fileName.replace(".xlsx", ".txt");
			
			if (!sourceFile.exists()) {
	            System.out.println("Error: File " + fileName + " not found in " + directoryPath);
	            System.exit(1);
	        }
			
			
		    Path source = Paths.get(directoryPath+"/"+filePath.toString());
		    Path target = Paths.get(targetPath);
		    Files.move(source, target, StandardCopyOption.REPLACE_EXISTING);
		    System.out.println("File renamed successfully using Files.move()");
		    
		    
		    
		} catch (Exception e) {
		    System.out.println("Error renaming file: " + e.getMessage());
		}
		
		
		
	}

}


