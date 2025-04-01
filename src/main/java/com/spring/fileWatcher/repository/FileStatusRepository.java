package com.spring.fileWatcher.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spring.fileWatcher.model.FileStatus;

@Repository
public interface FileStatusRepository extends JpaRepository<FileStatus, String> {

}
