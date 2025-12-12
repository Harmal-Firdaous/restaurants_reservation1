package ma.emsi.restaurantservice.config;

import ma.emsi.restaurantservice.entity.Menu;
import ma.emsi.restaurantservice.entity.Restaurant;
import ma.emsi.restaurantservice.entity.Review;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

@Configuration
public class DataRestConfig implements RepositoryRestConfigurer {

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {
        // expose IDs in JSON output
        config.exposeIdsFor(Restaurant.class);
        config.exposeIdsFor(Menu.class);
        config.exposeIdsFor(Review.class);

        // basePath : si tu veux exposer par exemple /api/restaurants
        config.setBasePath("/api");
    }
}