package ma.emsi.restaurantservice.entity;

import jakarta.persistence.*;
import lombok.*;
import ma.emsi.restaurantservice.enums.CuisineType;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "restaurants")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String address;
    private Double latitude;
    private Double longitude;
    @Enumerated(EnumType.STRING)
    private CuisineType cuisineType;

    @Column(name = "google_place_id")
    private String googlePlaceId;

    @Column(length = 1000)
    private String description;

    private String phoneNumber;
    private String website;

    @ElementCollection
    @CollectionTable(name = "restaurant_opening_hours", joinColumns = @JoinColumn(name = "restaurant_id"))
    @Column(name = "opening_hour")
    private List<String> openingHours;

    @OneToMany(
            mappedBy = "restaurant",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Menu> menus = new ArrayList<>();


    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews;

    // Calculate average rating
    public Double getAverageRating() {
        if (reviews == null || reviews.isEmpty()) {
            return null;
        }
        return reviews.stream()
                .mapToInt(Review::getRating)
                .average()
                .orElse(0.0);
    }
}