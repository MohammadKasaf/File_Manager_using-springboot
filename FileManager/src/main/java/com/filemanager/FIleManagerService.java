package com.filemanager;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

@Service
public class FIleManagerService {

    public static final String STORAGE_DIRECTORY="D:\\Users\\mohammad kasaf sidd\\IdeaProjects";

    //to save file at server
    public void saveFile(MultipartFile fileToSave) throws IOException {

        if (fileToSave == null) {
            throw new NullPointerException("fileToSave is null");
        }

        // Create target file path
        var targetFile = new File(STORAGE_DIRECTORY + File.separator + fileToSave.getOriginalFilename());

        // Check if the parent directory matches the expected STORAGE_DIRECTORY path
        if (!Objects.equals(targetFile.getParent(), STORAGE_DIRECTORY)) {
            throw new SecurityException("Unsupported filename");
        }

        // Save the file to the target location
        Files.copy(fileToSave.getInputStream(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }


   //to download file from server
    public File getDownloadFile(String fileName) throws IOException {

        if(fileName == null || fileName.trim().isEmpty()) {
            throw new IllegalArgumentException("File name is null or empty");
        }

        // Create the file object
        var fileToDownload = new File(STORAGE_DIRECTORY + File.separator + fileName);

        // Validate the parent directory
        if(!Objects.equals(fileToDownload.getParent(), STORAGE_DIRECTORY)) {
            throw new SecurityException("Invalid file path: " + fileName);
        }

        // Check if file exists
        if(!fileToDownload.exists()) {
            throw new FileNotFoundException("File not found: " + fileName);
        }

        return fileToDownload;
    }
}
