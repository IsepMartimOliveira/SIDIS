package com.example.psoft_22_23_project.devicemanagement.model;

import com.example.psoft_22_23_project.devicemanagement.model.Device;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import javax.persistence.*;

@Entity
@Setter
@Getter
public class DeviceImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String prefix;
    private String fileName;
    private String fileDownloadUri;
    private String contentType;
    private long fileSize;

    public DeviceImage() {
        // Default constructor required by JPA
    }

    public DeviceImage(String prefix, String fileName, String fileDownloadUri, String contentType, long fileSize) {
        this.prefix = prefix;
        this.fileName = fileName;
        this.fileDownloadUri = fileDownloadUri;
        this.contentType = contentType;
        this.fileSize = fileSize;
    }
}

