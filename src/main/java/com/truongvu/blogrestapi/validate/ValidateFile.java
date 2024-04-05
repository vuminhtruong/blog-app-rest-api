package com.truongvu.blogrestapi.validate;

import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Component
public class ValidateFile {
    public void validateFile(MultipartFile file) throws BadRequestException {
        String fileName = file.getOriginalFilename();
        if(fileName == null || fileName.isEmpty()) {
            System.out.println("File name: " + fileName);
            throw new BadRequestException("File name is empty!");
        }

        String fileExtension = getFileExtension(fileName);
        if(!checkFileExtension(fileExtension)) {
            System.out.println("File extension: " + fileExtension);
            throw new BadRequestException("File format is not correct!");
        }
    }

    // get PNG, JPG
    private String getFileExtension(String fileName) {
        int lastIndexOf = fileName.lastIndexOf(".");
        return fileName.substring(lastIndexOf + 1);
    }

    private boolean checkFileExtension(String fileExtension) {
        List<String> extensions = new ArrayList<>(List.of("png","jpg","jpeg","pdf"));
        return extensions.contains(fileExtension.toLowerCase());
    }
}
