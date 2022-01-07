package com.github.mtahasahin.evently.interfaces;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public interface FileStorageService {

    ByteArrayOutputStream downloadFile(String fileName);

    String uploadFile(String fileName, ByteArrayInputStream input);

    void deleteFile(String fileName);
}
