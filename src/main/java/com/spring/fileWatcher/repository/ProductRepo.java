package com.spring.fileWatcher.repository;

import org.springframework.stereotype.Repository;

import com.spring.fileWatcher.model.Product;



@Repository("productRepo")   // use name  which is suggested
public interface ProductRepo extends CommonRepo<Product, Integer> {

}

