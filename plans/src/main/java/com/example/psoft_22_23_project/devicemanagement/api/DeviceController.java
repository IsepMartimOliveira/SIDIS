package com.example.psoft_22_23_project.devicemanagement.api;

import com.example.psoft_22_23_project.devicemanagement.services.DeviceService;
import com.example.psoft_22_23_project.filestoragemanagement.api.UploadFileResponse;
import com.example.psoft_22_23_project.devicemanagement.model.DeviceImage;
import com.example.psoft_22_23_project.filestoragemanagement.service.FileStorageService;
import com.example.psoft_22_23_project.utils.Utils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

@Tag(name = "Devices", description = "Endpoints for managing devices")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/device")
public class DeviceController {

    private static final Logger logger = LoggerFactory.getLogger(DeviceController.class);

    private final DeviceService service;

    private final FileStorageService fileStorageService;

    private final DeviceViewMapper deviceViewMapper;


    private Long getVersionFromIfMatchHeader(final String ifMatchHeader) {
        if (ifMatchHeader.startsWith("\"")) {
            return Long.parseLong(ifMatchHeader.substring(1, ifMatchHeader.length() - 1));
        }
        return Long.parseLong(ifMatchHeader);
    }

    // the client determines the id of the resource since the client sends the name,
    // so POST to the collection
    @Operation(summary = "Creates a new device")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<DeviceView> create(
            @Valid @ModelAttribute final CreateDeviceRequest resource,
            @RequestParam(name = "file", required = false) final MultipartFile file)
            throws URISyntaxException {

        DeviceImage deviceImage = null;

        if (file != null) {
            deviceImage = doUploadFile(resource.getName(), file);
        }

        final var device = service.create(resource, deviceImage);

        final var newDeviceUri =
                ServletUriComponentsBuilder.fromCurrentRequestUri().pathSegment(device.getMacAddress().getMacAddress()
                ).build().toUri();

        return ResponseEntity.created(newDeviceUri).eTag(Long.toString(device.getVersion()))
                .body(deviceViewMapper.toDeviceView(device));
    }


    @Operation(summary = "Fully replaces an existing device")
    @PutMapping
    public ResponseEntity<DeviceView> upsert(final WebRequest request,
                                          @RequestParam("macAddress") @Parameter(description = "The id of the device to replace") final String macAddress,
                                          @Valid @ModelAttribute final EditDeviceRequest resource,
                                             @RequestParam(name = "file", required = false) final MultipartFile file)
            throws URISyntaxException {
        final String ifMatchValue = request.getHeader("If-Match");
        if (ifMatchValue == null || ifMatchValue.isEmpty()) {
            // no if-match header was sent, so we are in INSERT mode
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "You must issue a conditional PATCH using 'if-match'");
        }

        DeviceImage deviceImage = null;

        if (file != null) {
            deviceImage = doUploadFile(resource.getName(), file);
        }

        // if-match header was sent, so we are in UPDATE mode
        final var device = service.update(macAddress, resource, deviceImage, getVersionFromIfMatchHeader(ifMatchValue));
        return ResponseEntity.ok().eTag(Long.toString(device.getVersion())).body(deviceViewMapper.toDeviceView(device));
    }

    @Operation(summary = "Gets all user devices")
    @GetMapping("/all")
    public Iterable<DeviceView> findAllDevicesByUser() {
        return deviceViewMapper.toDevicesView(service.findAllDevicesByUser());
    }

    @Operation(summary = "Deletes an existing device")
    @DeleteMapping
    public ResponseEntity<DeviceView> delete(final WebRequest request,
                                          @RequestParam("macAddress")  final String macAddress) {
        final String ifMatchValue = request.getHeader("If-Match");
        if (ifMatchValue == null || ifMatchValue.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "You must issue a conditional DELETE using 'if-match'");
        }
        final int count = service.deleteDevice(macAddress, getVersionFromIfMatchHeader(ifMatchValue));

        if (count == 0) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } else if (count == 1) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @Operation(summary = "Downloads a photo of a device")
    @GetMapping("/photo/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable final String fileName,
                                                 final HttpServletRequest request) {
        // Load file as Resource
        final Resource resource = fileStorageService.loadFileAsResource(fileName);

        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (final IOException ex) {
            logger.info("Could not determine file type.");
        }

        // Fallback to the default content type if type could not be determined
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    public DeviceImage doUploadFile(final String id, final MultipartFile file) {

        final String fileName = fileStorageService.storeFile(id, file);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentRequestUri().pathSegment(fileName)
                .toUriString();
        // since we are reusing this method both for single file upload and multiple
        // file upload and have different urls we need to make sure we always return the
        // right url for the single file download
        fileDownloadUri = fileDownloadUri.replace("/photos/", "/photo/");

        return new DeviceImage(Utils.transformSpaces(id), fileName, fileDownloadUri, file.getContentType(), file.getSize());
    }


}
