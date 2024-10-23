package com.example.service.Components.utils;

import com.example.dto.ImageUploadRes;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

@Component
@Slf4j
public class FileUploader {

    public boolean pathExist(String rootPath){
        File file = new File(rootPath);
        return file.exists();
    }

    public boolean makeDir(String rootPath){
        File file = new File(rootPath);
        return file.mkdir();
    }

    @Autowired
    Exchanger exchanger;

    public ImageUploadRes uploadImage(String rootFolder, MultipartFile file, String uploaderLink){
        log.info(file.getContentType());
        //handle local files
        if(!this.pathExist(rootFolder)){
            log.info("rootfolder missed");

            ///CLOUD UPLOAD
            MultiValueMap<String, Object> formData = new LinkedMultiValueMap<>();

            formData.add("file", file.getResource());
            JsonNode node = exchanger.sendFormData(uploaderLink, formData);
            return ImageUploadRes.builder()
                    .fileName(node.get("url").asText())
                    .publicId(node.get("public_id").asText())
                    .build();
        }
        this.makeDir(rootFolder);
        String fileName =""+System.currentTimeMillis();
        try{
            fileName = fileName+"."+extPath(file);
            file.transferTo(Paths.get(rootFolder+"/"+fileName));
            return ImageUploadRes.builder()
                    .fileName(fileName)
                    .publicId(null)
                    .build();
        }catch(Exception e){
            log.info(e.getMessage());
            return null;
        }
    }

    public String extPath(MultipartFile file){
        return file.getContentType().split("/")[1];
    }

    public boolean deleteLocalPicture(String fileName){
        File file = new File(fileName);
        return file.delete();
    }

    public void deleteCloudPicture(String url,String publicId){
        exchanger.sendGetRequest(url+"/uploader/delete/"+publicId);
    }

    public ResponseEntity<byte[]> getPic(String fileName) {
        File file = new File(fileName);
        HttpHeaders headers = new HttpHeaders();
        if(this.getExtension(file).equals("jpeg")){
            headers.setContentType(MediaType.IMAGE_JPEG);
            log.info("file is jpeg");
        }else{
            headers.setContentType(MediaType.IMAGE_PNG);
            log.info("file is png");
        }
        try{
            String path = file.getAbsolutePath();
            byte[] bytes = Files.readAllBytes(Paths.get(path));
            return new ResponseEntity<>( bytes, headers, HttpStatus.valueOf(200));
        }catch(Exception e){
            log.warn(e.getLocalizedMessage());
            return null;
        }
    }
    public String getExtension(File file){
        log.info(file.getName());
        String filename = file.getName();
        if(filename.contains("jpeg")){
            return "jpeg";
        }
        return "png";
    }

}
