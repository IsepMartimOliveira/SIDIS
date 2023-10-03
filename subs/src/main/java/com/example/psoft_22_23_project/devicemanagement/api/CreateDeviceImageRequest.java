package com.example.psoft_22_23_project.devicemanagement.api;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Data
@RequiredArgsConstructor
@Getter
@Setter
public class CreateDeviceImageRequest {

    private String fileName;
    private String fileDownloadUri;
    private String contentType;
    private long fileSize;

}
