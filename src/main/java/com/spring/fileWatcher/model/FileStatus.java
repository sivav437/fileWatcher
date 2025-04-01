package com.spring.fileWatcher.model;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="fileStatus")
public class FileStatus {
	
	@Id
    private String fileId;
    private String status; // PENDING, PROCESSING, COMPLETED, FAILED
    private Date timestamp;
    private String originalFileName;
	public String getFileId() {
		return fileId;
	}
	public void setFileId(String fileId) {
		this.fileId = fileId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	public String getOriginalFileName() {
		return originalFileName;
	}
	public void setOriginalFileName(String originalFileName) {
		this.originalFileName = originalFileName;
	}
	public FileStatus(String fileId, String status, Date timestamp, String originalFileName) {
		super();
		this.fileId = fileId;
		this.status = status;
		this.timestamp = timestamp;
		this.originalFileName = originalFileName;
	}
	public FileStatus() {
		super();
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		return "FileStatus [fileId=" + fileId + ", status=" + status + ", timestamp=" + timestamp
				+ ", originalFileName=" + originalFileName + "]";
	}
    
    

}
