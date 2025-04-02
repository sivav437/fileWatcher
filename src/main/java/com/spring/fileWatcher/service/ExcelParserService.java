package com.spring.fileWatcher.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.spring.fileWatcher.model.Customer;
import com.spring.fileWatcher.model.ExcelSheet;
import com.spring.fileWatcher.model.Product;
import com.spring.fileWatcher.repository.CommonRepo;



@Service 
public class ExcelParserService {
	
	
	
	private final ApplicationContext context;

    public ExcelParserService(ApplicationContext context) {
        this.context = context;
    }
	
	
	List<Customer> customers=new ArrayList<>();
    
    List<Product> products=new ArrayList<>();
	
    // note : should not declare entity here coz it will re-update all.
	
	
	public List<List<ExcelSheet>> parseExcelFile(String filePath) throws IOException 
	{ 
		
		System.out.println(filePath+" : recieved filePath at parseExcelFile");
		
		Path fp=Paths.get(filePath);

        return parsingExcelCode(fp.toFile());
                
	}  
	
	
	private List<List<ExcelSheet>> parsingExcelCode(File file) throws IOException{ //InputStream inputStream
		
		System.out.println("entered into parsingExcelCode ");
		
		List<List<ExcelSheet>> allData = new ArrayList<>();
		
		CommonRepo<? extends ExcelSheet, Integer> repository = null;
		
		try (Workbook workbook = WorkbookFactory.create(file)) { //inputStream
        	
        	
        	int num_sheets=workbook.getNumberOfSheets();
        	int idx=0;
        	
        	//System.out.println(num_sheets+" no. of sheets");
        	
        	while(idx<num_sheets) {
        		
        	
               Sheet sheet = (Sheet) workbook.getSheetAt(idx);
               
               String sheet_name=sheet.getSheetName();
               
               switch(sheet_name) 
               {
               
               		case "customerData":

               			 repository = (CommonRepo<Customer, Integer>) context.getBean("customerRepo");
               			 
               			 break;
               			
               		case "productData":
               			
               			repository = (CommonRepo<Product, Integer>) context.getBean("productRepo");
               			
               			break;
               		
               	}
               

               int start_row=sheet.getFirstRowNum();
               int end_row=sheet.getLastRowNum();
               
               Row row_header=sheet.getRow(0);
               
               String[] headers=new String[row_header.getPhysicalNumberOfCells()];
               
               int  start_cell_header=row_header.getFirstCellNum();
         	   int  end_cell_header=row_header.getLastCellNum();
         	   
         	  for(;start_cell_header<end_cell_header;start_cell_header++) {
         		  
        		  Cell cell=row_header.getCell(start_cell_header);
        		  
        		  switch(cell.getCellType()) {
        		  
        		  case STRING:
                       headers[start_cell_header]= cell.getStringCellValue();
                       break;
                  
        		  }
         	  }
         	  
         	  
         	   
               
               for(;start_row<=end_row;start_row++) {
            	   if( !(start_row>0)) 
            	   { 
            		   continue;
            	   }
            	   
            	   Row row=sheet.getRow(start_row);
            	  
            	   int  start_cell=row.getFirstCellNum();
            	   int  end_cell=row.getLastCellNum();
            	  
            	  Customer localCust = null;
            	  Product localProd = null;
            	  
            	  if(sheet_name.equals("customerData")) 
            	  {
            		  localCust=new Customer();
            		  
            	  }
            	  else if(sheet_name.equals("productData")) 
            	  {
            		  localProd=new Product();
            		  
            	  }
            	  
            	  for(;start_cell<end_cell;start_cell++) 
            	  {
            		  
            		  Cell cell=row.getCell(start_cell);

            		  switch(headers[start_cell]) 
            		  {
            	 
            		  	case "customer_id":
            			  
            		  		int customer_id=(int) getCellValue(cell,"customer_id");
            		  		localCust.setCustomer_id(customer_id);
            		  		break;
            			  
            		  	case "customer_name":
            			  
            		  		String customer_name=(String) getCellValue(cell,"customer_name");
            		  		localCust.setCustomer_name(customer_name);
            		  		break;
            			  
            		  	case "customer_password":
            			  
            		  		String customer_password=(String) getCellValue(cell,"customer_password");
            		  		localCust.setCustomer_password(customer_password);
            		  		break;
            			  
            		  	case "product_id":
            			  
            		  		int product_id=(int) getCellValue(cell,"product_id");
            		  		localProd.setProduct_id(product_id);
            		  		break;
            			  
            		  	case "product_name":
            			  
            		  		String product_name=(String) getCellValue(cell,"product_name");
            		  		localProd.setProduct_name(product_name);
            		  		break;
            			  
            		  	case "product_price":
            			  
            		  		int product_price=(int) getCellValue(cell,"product_price");
            		  		localProd.setProduct_price(product_price);
            		  		break;
            		 
            		  }
            	  }
            	  
            	  if(localCust != null && localCust.getCustomer_id()>0) {

            		  customers.add(localCust);
            		 
            	  }
            	  
            	  if(localProd !=null && localProd.getProduct_id()>0 ) {
            		  products.add(localProd);
            	  }
            	  
            	  
            	  
            	   
               }
               
             
               idx++;
        } //while block
               
        if(customers!=null) { 
        	
        	allData.add(new ArrayList<ExcelSheet>(customers));
        	
        	
        	CommonRepo<Customer, Integer> customerRepo = (CommonRepo<Customer, Integer>) repository;
            
        	customerRepo.saveAll( customers);
        	
        	}
        
        if(products != null) 
        {
        	
        	allData.add(new ArrayList<ExcelSheet>(products));
        	
        	CommonRepo<Product, Integer> productRepo = (CommonRepo<Product, Integer>) repository;
            
        	productRepo.saveAll(products);
        	
        	}
        
        
           return allData;
        
	} // try block

	}
	
	
	private Object getCellValue(Cell cell,String headerName) {
		
		if (cell == null) {
            return null;
        }
		switch(cell.getCellType()) {
		
		 case STRING:
             return cell.getStringCellValue();
		 case NUMERIC:
             if (DateUtil.isCellDateFormatted(cell)) {
                 return cell.getDateCellValue();
             }
             if(headerName=="customer_id" || headerName=="product_id"  || headerName=="product_price") {
            	 return (int)cell.getNumericCellValue();
             }
             return cell.getNumericCellValue();
		 case BOOLEAN:
             return String.valueOf(cell.getBooleanCellValue());
		 case FORMULA:
             return cell.getCellFormula();
		 case BLANK:
			 return null;
         default:
        	 return null;
		
		}

		
	}
	

	
//	public List<List<ExcelSheet>> parseExcelFromUpload(MultipartFile file) throws IOException
//	{
//		
//		InputStream inputStream = file.getInputStream();
//		
//		return parsingExcelCode(inputStream);
//			
//	}
	
	
	
}
	