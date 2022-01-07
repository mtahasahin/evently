package com.github.mtahasahin.evently.controller;

import com.github.mtahasahin.evently.interfaces.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequiredArgsConstructor
public class FileController {

    private final FileStorageService fileUploadService;

    @GetMapping(value = "/file/**", produces = {"image/jpeg", "image/png"})
    public ResponseEntity<byte[]> getFile(HttpServletRequest request) {
        var requestedUri = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
        return ResponseEntity.ok(fileUploadService.downloadFile(requestedUri.substring(6)).toByteArray());
    }
}
