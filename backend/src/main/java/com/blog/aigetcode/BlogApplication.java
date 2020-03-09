package com.blog.aigetcode;

import com.blog.aigetcode.properties.AuthenticationProperties;
import com.blog.aigetcode.properties.FileStorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.blog.aigetcode")
@EnableConfigurationProperties({
        FileStorageProperties.class, AuthenticationProperties.class
})
public class BlogApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlogApplication.class, args);
    }
}
