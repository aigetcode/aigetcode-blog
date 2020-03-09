package com.blog.aigetcode.config;

import com.blog.aigetcode.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@ControllerAdvice
public class NotFoundHandler {

    @Value("${spa.default-file}")
    String defaultFile;

    private FileService fileService;

    @Autowired
    public NotFoundHandler(FileService fileService) {
        this.fileService = fileService;
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<?> renderDefaultPage(final NoHandlerFoundException ex, HttpServletRequest req, RedirectAttributes redirectAttributes) {
        try {
            if (ex.getRequestURL().equals("/index.html") || !ex.getRequestURL().contains(".")) {
                File indexFile = ResourceUtils.getFile(defaultFile);
                FileInputStream inputStream = new FileInputStream(indexFile);
                String body = StreamUtils.copyToString(inputStream, Charset.defaultCharset());
                return ResponseEntity.ok().contentType(MediaType.TEXT_HTML).body(body);
            } else {
                String[] splitUrlRequest = ex.getRequestURL().split("/");
                List<CharSequence> list = new LinkedList<>();
                if (splitUrlRequest.length > 2) {
                    list = new LinkedList<>(Arrays.asList(splitUrlRequest).subList(0, splitUrlRequest.length - 1));
                }

                String fileName = splitUrlRequest[splitUrlRequest.length - 1];
                String pathToFile = String.join("/", list) + "/";

                Resource resource = fileService.loadFileAsResourceByClassPath(fileName, pathToFile);
                String contentType = fileService.getContentTypeFile(req, resource);

                String[] splitString = contentType.split("/");
                MediaType mediaType = new MediaType(splitString[0], splitString[1]);
                return ResponseEntity
                        .ok()
                        .contentType(mediaType)
                        .body(resource);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("There was an error completing the action.");
        }
    }
}
