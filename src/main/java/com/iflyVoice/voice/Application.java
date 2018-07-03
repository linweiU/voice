package com.iflyVoice.voice;

import javax.servlet.MultipartConfigElement;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;

@SuppressWarnings("deprecation")
@SpringBootApplication
@MapperScan("com.iflyVoice.voice.mapper")
public class Application extends SpringBootServletInitializer {
    public static final Logger log = Logger.getLogger("Application.class");
    static {
        try {
            // 初始化log4j
            String log4jPath = Application.class.getClassLoader().getResource("").getPath()
                    + "log4j.properties";
            // log.debug("初始化Log4j。。。。");
            // log.debug("path is " + log4jPath);
            PropertyConfigurator.configure(log4jPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        log.debug("============= SpringBoot Start Success =============");
    }

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize("50MB");
        factory.setMaxRequestSize("50MB");
        return factory.createMultipartConfig();
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }
}