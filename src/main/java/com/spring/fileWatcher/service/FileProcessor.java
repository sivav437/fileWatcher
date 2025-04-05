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
	
	private String fileToParse(String filePath) {
		String baseDirectory = "D:\\ExcelFiles";
		String fileToParse = baseDirectory+"/"+filePath;
		return fileToParse;
		
	}
	
	public void processFile(Path filePath) throws IOException  {
		
		//System.out.println(filePath.toAbsolutePath().toString()+" : filePath ABSOLUTE");
		
		String file_Path_Str=filePath.toString();
		String fileToParse=this.fileToParse(file_Path_Str);
		
		try {
		service.parseExcelFile(fileToParse);
		
		}catch(Exception ee) {
			System.out.println("Error occured while parsing excel "+ee.getMessage());
		}
		
		int idx=filePath.toString().indexOf(".xlsx");
		
		String str1=filePath.toString().substring(0, idx);
		
		String newFilePath = str1 + ".txt";
		
		//System.out.println("Running as user: " + System.getProperty("user.name"));
		
		
		try {
			
//			String directoryPath = "/home/ctuser/ExcelFiles"; // hardcoded
			
			String baseDirectory = "D:\\ExcelFiles";
			
//			String fileName=filePath.toString();
			
			File directory = new File(baseDirectory);
			File sourceFile = new File(baseDirectory, file_Path_Str);
			
			String targetPath = baseDirectory+"/"+file_Path_Str.replace(".xlsx", ".txt");
			
			if (!sourceFile.exists()) {
	            System.out.println("Error: File " + file_Path_Str + " not found in " + baseDirectory);
	            System.exit(1);
	        }
			
			
		    Path source = Paths.get(baseDirectory+"/"+filePath.toString());
		    Path target = Paths.get(targetPath);
		    Files.move(source, target, StandardCopyOption.REPLACE_EXISTING);
		    System.out.println("File renamed successfully using Files.move()");
		    
		    
		    
		} catch (Exception e) {
		    System.out.println("Error renaming file: " + e.getMessage());
		}
		
		
		
	}

}


