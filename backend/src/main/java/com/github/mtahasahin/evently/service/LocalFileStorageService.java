package com.github.mtahasahin.evently.service;

import com.github.mtahasahin.evently.interfaces.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
@Profile("!s3")
public class LocalFileStorageService implements FileStorageService {
    private final ServletWebServerApplicationContext server;

    private final static Path root = Paths.get("uploads");

    @Override
    public ByteArrayOutputStream downloadFile(String fileName) {
        try {
            var file = Files.readAllBytes(root.resolve(fileName));
            var result = new ByteArrayOutputStream();
            result.write(file);
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String uploadFile(String key, ByteArrayInputStream input) {
        try {
            Files.createDirectories(root.resolve(key).getParent());
            Files.write(root.resolve(key), input.readAllBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        var currentRequest = ServletUriComponentsBuilder.fromCurrentContextPath();
        return currentRequest. path("/file/{fileName}").buildAndExpand(key).toUriString();
    }

    @Override
    public void deleteFile(String fileName) {

    }
}
