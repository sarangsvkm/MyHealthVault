package in.myhealthvault.config;

import org.springframework.context.annotation.*;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
            .allowedMethods("GET","POST","PUT","PATCH","DELETE")
            .allowedHeaders("Authorization","Content-Type")
            .allowedOrigins("http://localhost:8081");
    }
}
