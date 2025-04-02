package com.spring.fileWatcher.repository;

import org.springframework.stereotype.Repository;

import com.spring.fileWatcher.model.Customer;


@Repository("customerRepo") // use name  which is suggested
public interface CustomerRepo extends CommonRepo<Customer, Integer> {

}

