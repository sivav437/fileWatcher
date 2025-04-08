package com.spring.fileWatcher.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
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
	
	
	private Iterator<Sheet> findingSheets(Workbook workbook){
		return workbook.iterator();
	}
	
	private CommonRepo<? extends ExcelSheet,Integer> createRepositoryBasedOnSheetName(String sheet_name){
		CommonRepo<? extends ExcelSheet,Integer> repository = null;
		if(sheet_name.equalsIgnoreCase("customerData")) {
			 repository = (CommonRepo<Customer, Integer>) context.getBean("customerRepo");
		}else if(sheet_name.equalsIgnoreCase("productData")) {
			repository = (CommonRepo<Product, Integer>) context.getBean("productRepo");
		}
		return repository;
	}
	
	
	private String[] findCurrentSheetHeaders(Sheet current_sheet) {
		
		Row row_header=current_sheet.getRow(0);
		String[] headers=new String[row_header.getPhysicalNumberOfCells()];
		
		int  start_cell_header=row_header.getFirstCellNum();
  	    int  end_cell_header=row_header.getLastCellNum();
  	   
  	    for(;start_cell_header<end_cell_header;start_cell_header++) {
  		  
 		  Cell cell=row_header.getCell(start_cell_header);
 		  
 		  if(cell.getCellType().toString().equals("STRING")) {
 			 headers[start_cell_header]= cell.getStringCellValue();
 		  }
 		 
  	  }
		return headers;
	}
	
	
	private ExcelSheet createEntityBasedOnSheetName(String sheet_name) {
		
		if(sheet_name.equals("customerData")) 
  	  {
  		  return new Customer();
  		  
  	  }
  	  else if(sheet_name.equals("productData")) 
  	  {
  		  return new Product();
  		  
  	  }
		return null;
	}
	
	
	private Customer ExtractCustomersDataFromCell(Row row,String[] headers,CommonRepo<? extends ExcelSheet,Integer> repository,int id) {
		
		 int  start_cell=row.getFirstCellNum();
  	   	 int  end_cell=row.getLastCellNum();
  	   	 
  	   	 Customer localCust=new Customer();
  	   	 CommonRepo<Customer, Integer> customerRepo =  (CommonRepo<Customer, Integer>) repository;
  	   	 
  	   for(;start_cell<end_cell;start_cell++) 
 	  {
 		  
 		  Cell cell=row.getCell(start_cell);

 		  switch(headers[start_cell]) 
 		  {
 	 
 		  	case "customer_id":
 			  
 		  		int customer_id=(int) getCellValue(cell,"customer_id");
// 		  		int id=(int)repository.count();
// 		  		System.out.println(id+"cust id");
 		  		//localCust.setCustomer_id(id);
 		  		break;
 			  
 		  	case "customer_name":
 			  
 		  		String customer_name=(String) getCellValue(cell,"customer_name");
 		  		localCust.setCustomer_name(customer_name);
 		  		break;
 			  
 		  	case "customer_password":
 			  
 		  		String customer_password=(String) getCellValue(cell,"customer_password");
 		  		localCust.setCustomer_password(customer_password);
 		  		break;
 			  
 		  }
 	  }
  	   
  	   
  	   return localCust;
  	   	 
		
	}
	
	
	private Product ExtractProductsDataFromCell(Row row,String[] headers,CommonRepo<? extends ExcelSheet,Integer> repository,int id) {
		
		 int  start_cell=row.getFirstCellNum();
 	   	 int  end_cell=row.getLastCellNum();
 	   	 
 	   	 Product localProd=new Product();
 	  
 	   	for(;start_cell<end_cell;start_cell++) 
  	  {
  		  
  		  Cell cell=row.getCell(start_cell);

  		  switch(headers[start_cell]) 
  		  {
  			  
  		  	case "product_id":
  			  
  		  		int product_id=(int) getCellValue(cell,"product_id");
  		  		
//  		  		System.out.println(id+" : id of repo prod");
  		  		//localProd.setProduct_id(id);
  		  		id+=1;
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
 	   	
 	   	return localProd; // 
 	   	 
		
	}
	
	
	private void ExtractCellsFromRow(Sheet current_sheet,String[] headers,CommonRepo<? extends ExcelSheet,Integer> repository) {
		
		int start_row=current_sheet.getFirstRowNum();
        int end_row=current_sheet.getLastRowNum();
        
        String sheet_name=current_sheet.getSheetName();
        
        int id=(int)repository.count();
        for(start_row=1;start_row<=end_row;start_row++) {
     	   
     	   Row row=current_sheet.getRow(start_row);
     	  
     	   int  start_cell=row.getFirstCellNum();
     	   int  end_cell=row.getLastCellNum();
     	  
     	  ExcelSheet localEntity= this.createEntityBasedOnSheetName(sheet_name);
     	  
     	  Customer localCust = null;
     	  Product localProd = null;
     	  
     	  if(sheet_name.equalsIgnoreCase("customerData")) {
         	  localCust=this.ExtractCustomersDataFromCell(row, headers,repository,id+=1);
         	  System.out.println(localCust+" localCust");
         	  localProd = null;
     	  }else if(sheet_name.equalsIgnoreCase("productData")) {
         	  localProd=this.ExtractProductsDataFromCell(row, headers,repository,id+=1);
         	 System.out.println(localCust+" productData");
         	  localCust = null;
     	  }else {
     		  localCust = null;
         	  localProd = null;
     	  }
     	  
     	 if(localCust != null ) {//&& localCust.getCustomer_id()>0
     	  System.out.println(localCust+" localCust added to customers");
   		  customers.add(localCust);
   		 
   	  }
   	  
   	  if(localProd !=null  ) { //&& localProd.getProduct_id()>0
   		System.out.println(localProd+" localProd added to products");
   		  products.add(localProd);
   	  }
     	  
        }

		
	}
	
	
	private void addDataToDataBase(List<List<ExcelSheet>> allData,CommonRepo<? extends ExcelSheet,Integer> repository) {
		
		
		
		
		if(customers!=null) { 
			
        	allData.add(new ArrayList<ExcelSheet>(customers));
        	
        	System.out.println(" customers allData" +allData);
			CommonRepo<Customer, Integer> customerRepo =  (CommonRepo<Customer, Integer>) repository;
            
        	customerRepo.saveAll( customers);
        	
        	}
        
        if(products != null) 
        {
        	
        	allData.add(new ArrayList<ExcelSheet>(products));
        	System.out.println(" products allData " +allData);
        	CommonRepo<Product, Integer> productRepo = (CommonRepo<Product, Integer>) repository;
            
        	productRepo.saveAll(products);
        	
        	}
       // return allData;
	}
	
	private List<List<ExcelSheet>> parsingExcelCode(File file) throws IOException{ //InputStream inputStream
		
		System.out.println("entered into parsingExcelCode ");
		
		List<List<ExcelSheet>> allData = new ArrayList<>();
		
		
		try (Workbook workbook = WorkbookFactory.create(file)) { //inputStream
        	
        	
//        	int num_sheets=workbook.getNumberOfSheets();
//        	int idx=0;
        	
        	CommonRepo<? extends ExcelSheet,Integer> repository=null; // have to make null
        	
        	Iterator<Sheet> sheetItr=this.findingSheets(workbook);
        	
        	while(sheetItr.hasNext()) {
        		
               Sheet current_sheet = sheetItr.next();
               
               String sheet_name=current_sheet.getSheetName();
               
              repository=this.createRepositoryBasedOnSheetName(sheet_name);
               
               String[] headers= this.findCurrentSheetHeaders(current_sheet);
               
               
               this.ExtractCellsFromRow(current_sheet, headers,repository);
               
             
        } //while block
               
          System.out.println(allData+" allData");
          this.addDataToDataBase(allData, repository);
        
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
	

	
	
	
	
}
	