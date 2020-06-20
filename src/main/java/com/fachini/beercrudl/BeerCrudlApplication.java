package com.fachini.beercrudl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

import com.fachini.beercrudl.property.FileStorageProperties;

import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.spring.data.rest.configuration.SpringDataRestConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

@SpringBootApplication
@EnableSwagger2WebMvc
@Import({ SpringDataRestConfiguration.class, BeanValidatorPluginsConfiguration.class })
@EnableConfigurationProperties({ FileStorageProperties.class })
public class BeerCrudlApplication {

    public static void main(String[] args) {
        SpringApplication.run(BeerCrudlApplication.class, args);
    }

}
