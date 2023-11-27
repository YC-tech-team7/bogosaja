package com.YCtechAcademy.bogosaja.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    //이미지 파일 핸들링(업로드) 기능 - 파일 업로드 경로 포함 [각자 로컬 컴에서 테스트하려면 경로 변경해야 작동]
    //(Modification required - 아마 application.properties에서 변경하면 이건 자동 변경될겁니다)
    @Value("${uploadPath}")
    String uploadPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry){
        registry.addResourceHandler("/images/**")
                .addResourceLocations(uploadPath);
    }
}
