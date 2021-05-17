package com.example.tinkoffproject.configurations

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry

import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

import org.springframework.web.servlet.config.annotation.EnableWebMvc




@Configuration
class DocConfig {
    @Bean
    fun api(): Docket {
        return Docket(DocumentationType.SWAGGER_2)
            .select()
            .apis(RequestHandlerSelectors.any())
            .paths(PathSelectors.any())
            .build()
    }
}

@Configuration
@EnableWebMvc
class MVCConfig : WebMvcConfigurer {
    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        registry.addResourceHandler("/static/**")
            .addResourceLocations("classpath:/static/")
//        registry.addResourceHandler("/images/**")
//            .addResourceLocations("classpath:/static/images/")
//        registry.addResourceHandler("/js/**")
//            .addResourceLocations("classpath:/static/js/")
    }
}