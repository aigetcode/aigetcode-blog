package com.blog.aigetcode.service;

import com.blog.aigetcode.service.model.ImagesEntry;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

public interface FileService {

    String storeFile(MultipartFile file);
    Resource loadFileAsResource(String fileName);
    Resource loadFileAsResourceByClassPath(String fileName, String pathToFile);
    String getContentTypeFile(HttpServletRequest request, Resource resource);
    ImagesEntry changeContentUrlOnLocal(String content, String localPathImage);
    boolean deleteFile(String nameFile);

}
