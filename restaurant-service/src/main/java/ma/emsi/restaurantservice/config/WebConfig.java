package ma.emsi.restaurantservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // CORS is now configured in DataRestConfig to avoid duplicate headers
        // Uncomment this if you need CORS for non-Data REST endpoints
        /*
         * registry.addMapping("/**")
         * .allowedOrigins("http://localhost:5173")
         * .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
         * .allowedHeaders("*")
         * .allowCredentials(true);
         */
    }
}
