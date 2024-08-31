package com.filemanager;

import org.apache.tomcat.util.file.ConfigurationSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
public class FIleManagerController {

    @Autowired
    private FIleManagerService fIleManagerService;
    private Logger log=Logger.getLogger(FIleManagerController.class.getName());


    @PostMapping("/upload-file")
    public String uploadFile(@RequestParam("file")MultipartFile file){

        try {
            fIleManagerService.saveFile(file);
            return "file successfully uploaded";
        }
        catch (IOException e){
            log.log(Level.SEVERE,"Exception during upload", e.getMessage());
        }

        return "Error in uploading file";
    }

    @GetMapping("/downloadFile")
    public ResponseEntity<Resource> getFile(@RequestParam("fileName")String filename){
        log.log(Level.INFO, "[NORMAL] Download with /download");

        try{
          var fileToDownload=fIleManagerService.getDownloadFile(filename);
          return ResponseEntity.ok()
                  .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileToDownload.getName() + "\"")
                  .contentLength(fileToDownload.length())
                  .contentType(MediaType.APPLICATION_OCTET_STREAM)
                  .body(new InputStreamResource(Files.newInputStream(fileToDownload.toPath())));
        }
        catch(Exception e){
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/download-faster")
    public ResponseEntity<Resource> downloadFileFaster(@RequestParam("fileName") String filename) {
        log.log(Level.INFO, "[FASTER] Download with /download-faster");
        try {
            var fileToDownload =fIleManagerService.getDownloadFile(filename);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                    .contentLength(fileToDownload.length())
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    //for faster download
                    .body(new FileSystemResource(fileToDownload));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

}
