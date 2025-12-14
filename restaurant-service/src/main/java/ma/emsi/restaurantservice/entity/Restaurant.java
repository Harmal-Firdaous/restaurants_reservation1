package ma.emsi.restaurantservice.entity;

import jakarta.persistence.*;
import lombok.*;
import ma.emsi.restaurantservice.enums.CuisineType;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "restaurants")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Restaurant {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String address;
    private Double latitude;
    private Double longitude;
    private String googlePlaceId;

    @Enumerated(EnumType.STRING)
    private CuisineType cuisineType;

    @Column(columnDefinition = "DOUBLE DEFAULT 0.0")
    @Builder.Default
    private Double averageRating = 0.0;

    // ADD orphanRemoval = true
    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Review> reviews = new ArrayList<>();

    // ADD menus relationship with cascade
    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Menu> menus = new ArrayList<>();

    public void calculateAverageRating() {
        if (reviews == null || reviews.isEmpty()) {
            this.averageRating = 0.0;
            return;
        }

        double sum = reviews.stream()
                .mapToInt(Review::getRating)
                .average()
                .orElse(0.0);

        this.averageRating = Math.round(sum * 10.0) / 10.0;
    }
}