package com.example.loadbalancer.filestoragemanagement.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

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
