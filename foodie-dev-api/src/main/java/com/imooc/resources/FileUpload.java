package com.imooc.resources;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@Data
@PropertySource("classpath:file-upload-dev.properties")
@ConfigurationProperties(prefix = "file")
public class FileUpload {

    private String imageUserFaceLocation;

    private String imageServerUrl;


}
