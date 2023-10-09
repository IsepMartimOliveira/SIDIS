package com.example.psoft_22_23_project.filestoragemanagement.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@ConfigurationProperties("file")
@Configuration
public class FileStorageProperties {
	private String uploadDir;

	public FileStorageProperties(@Value("${file.upload-dir}") String uploadDir) {
		this.uploadDir = uploadDir;
	}

	public String getUploadDir() {
		return uploadDir;
	}

	public void setUploadDir(String uploadDir) {
		this.uploadDir = uploadDir;
	}
}
