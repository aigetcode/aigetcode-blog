package com.blog.aigetcode.service.impl;

import com.blog.aigetcode.exceptions.Check;
import com.blog.aigetcode.exceptions.FileStorageException;
import com.blog.aigetcode.exceptions.MyFileNotFoundException;
import com.blog.aigetcode.properties.FileStorageProperties;
import com.blog.aigetcode.service.FileService;
import com.blog.aigetcode.service.model.ImagesEntry;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Transactional
public class FileServiceImpl implements FileService {

    private final Path fileStorageLocation;

    @Autowired
    public FileServiceImpl(FileStorageProperties fileStorageProperties) {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
                .toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    @Override
    public String storeFile(MultipartFile file) {
        Date date = new Date();
        Check.required(file, "Required file parameter");
        Check.required(file.getOriginalFilename(), "Required file parameter");
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            String uniqueFileName = getMd5(fileName + date.getTime())
                    + "."
                    + FilenameUtils.getExtension(fileName).toLowerCase();
            if(uniqueFileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + uniqueFileName);
            }

            // Copy file to the target location (Replacing existing file with the same name)
            Path targetLocation = this.fileStorageLocation.resolve(uniqueFileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return uniqueFileName;
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    private static String getMd5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            StringBuilder hashText = new StringBuilder(no.toString(16));

            while (hashText.length() < 32) {
                hashText.insert(0, "0");
            }
            return hashText.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new MyFileNotFoundException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new MyFileNotFoundException("File not found " + fileName, ex);
        }
    }

    @Override
    public Resource loadFileAsResourceByClassPath(String fileName, String pathToFile) {
        try {
            String defaultPathToFile = "static/";
            if (!StringUtils.isEmpty(pathToFile)) {
                pathToFile = defaultPathToFile + pathToFile;
            }

            URL url = ResourceUtils.getURL(pathToFile);
            Path classpathStorageLocation = Paths.get(url.toURI())
                    .toAbsolutePath().normalize();
            Path filePath = classpathStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists()) {
                return resource;
            } else {
                File file = findFileByName(classpathStorageLocation.toString(), fileName);
                if (file != null) {
                    resource = new UrlResource(file.toURI());
                    if (resource.exists()) {
                        return resource;
                    }
                }

                throw new MyFileNotFoundException("File not found " + fileName);
            }
        } catch (MalformedURLException | FileNotFoundException | URISyntaxException ex) {
            throw new MyFileNotFoundException("File not found " + fileName, ex);
        }
    }

    private File findFileByName(String rootSearch, String fileName) {
        File root = new File(rootSearch);
        try {
            boolean recursive = true;
            Collection files = FileUtils.listFiles(root, null, recursive);

            for (Iterator iterator = files.iterator(); iterator.hasNext();) {
                File file = (File) iterator.next();
                if (file.getName().equals(fileName)) {
                    System.out.println(file.getAbsolutePath());
                    return file;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public String getContentTypeFile(HttpServletRequest request, Resource resource) {
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(contentType == null) {
            contentType = "application/octet-stream";
        }

        return contentType;
    }

    @Override
    public ImagesEntry changeContentUrlOnLocal(String content, String localPathImage) {
        Date date = new Date();
        List<String> images = new LinkedList<>();
        Pattern patternImage = Pattern.compile("<img.*?src=\"(.*?)\"");
        Matcher matcher = patternImage.matcher(content);

        while (matcher.find()) {
            String oldUrl = matcher.group(1);
            if (oldUrl.startsWith("http")) {
                String[] fileNameUrl = oldUrl.split("/");
                String fileName = oldUrl.split("/")[fileNameUrl.length - 1];
                String uniqueFileName = getMd5(fileName + date)
                        + "."
                        + FilenameUtils.getExtension(fileName).toLowerCase();
                String destinationFile = this.fileStorageLocation.toString() + File.separator + uniqueFileName;
                saveImageByUrl(oldUrl, destinationFile);
                images.add(uniqueFileName);

                String newUrl = localPathImage + uniqueFileName;
                content = content.replace(oldUrl, newUrl);
            }
        }
        return new ImagesEntry(content, images);
    }

    private void saveImageByUrl(String imageUrl, String destinationFile) {
        try {
            URL url = new URL(imageUrl);
            try(InputStream is = url.openStream(); OutputStream os = new FileOutputStream(destinationFile)) {
                byte[] b = new byte[2048];
                int length;

                while ((length = is.read(b)) != -1) {
                    os.write(b, 0, length);
                }
            }
        } catch (IOException e) {
            Check.fail("Error save file from news");
        }
    }

    @Override
    public boolean deleteFile(String nameFile) {
        try {
            if (nameFile != null) {
                Path fileToDeletePath = this.fileStorageLocation.resolve(nameFile).normalize();
                Files.delete(fileToDeletePath);
                return true;
            }
            return false;
        } catch (IOException e) {
            return false;
        }
    }
}
