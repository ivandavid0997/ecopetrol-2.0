package com.nttdata.ecopetrol.talento.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/talento/**")
                .allowedOrigins(
                        "http://localhost:4200",
                        "https://kind-moss-0208c3e10.7.azurestaticapps.net",
                        "https://app-central-ecopetrol-geeqdnayfth9d7cx.centralus-01.azurewebsites.net"
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .exposedHeaders("X-Session-ID")
                .allowCredentials(false);
    }
}
