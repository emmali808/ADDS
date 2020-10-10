package com.java.adds.config;

import com.java.adds.security.interceptor.AuthenticationInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Value("${file.path.deep-model-article}")
    private String deepModelArticleFolder;

    @Value("${file.path.deep-model-code}")
    private String deepModelCodeFolder;

    @Value("${file.path.deep-model-img}")
    private String deepModelImgFolder;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authenticationInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/model-article/**")
                .excludePathPatterns("/model-code/**")
                .excludePathPatterns("/model-img/**");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/model-article/**")
                .addResourceLocations("file:" + deepModelArticleFolder);
        registry.addResourceHandler("/model-code/**")
                .addResourceLocations("file:" + deepModelCodeFolder);
        registry.addResourceHandler("/model-img/**")
                .addResourceLocations("file:" + deepModelImgFolder);
    }

    @Bean
    public AuthenticationInterceptor authenticationInterceptor() {
        return new AuthenticationInterceptor();
    }
}
