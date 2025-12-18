package ma.emsi.restaurantservice;

import ma.emsi.restaurantservice.dto.MenuDto;
import ma.emsi.restaurantservice.dto.PlatDto;
import ma.emsi.restaurantservice.dto.RestaurantDto;
import ma.emsi.restaurantservice.service.MenuService;
import ma.emsi.restaurantservice.service.PlatService;
import ma.emsi.restaurantservice.service.RestaurantService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private RestaurantService restaurantService;
    @Autowired
    private MenuService menuService;
    @Autowired
    private PlatService platService;

    @Override
    public void run(String... args) throws Exception {
        if (!restaurantService.getAll().isEmpty()) {
            return;
        }

        // Restaurant 1
        RestaurantDto r1 = RestaurantDto.builder()
                .name("Tasty Burger")
                .address("123 Main St")
                .averageRating(4.5)
                .build(); // Add more fields if needed
        r1 = restaurantService.create(r1);

        MenuDto m1 = MenuDto.builder().title("Lunch Menu").build();
        m1 = menuService.create(r1.getId(), m1);

        PlatDto p1 = PlatDto.builder().name("Cheeseburger").description("Classic cheese burger").price(12.99).build();
        platService.create(m1.getId(), p1);
        PlatDto p2 = PlatDto.builder().name("Fries").description("Crispy fries").price(4.99).build();
        platService.create(m1.getId(), p2);

        // Restaurant 2
        RestaurantDto r2 = RestaurantDto.builder()
                .name("Pizza Heaven")
                .address("456 Olive Ave")
                .averageRating(4.8)
                .build();
        r2 = restaurantService.create(r2);

        MenuDto m2 = MenuDto.builder().title("Dinners").build();
        m2 = menuService.create(r2.getId(), m2);

        PlatDto p3 = PlatDto.builder().name("Pepperoni Pizza").description("Spicy pepperoni").price(15.99).build();
        platService.create(m2.getId(), p3);
    }
}
