package ma.emsi.restaurantservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Menu {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @ManyToOne
    private Restaurant restaurant;

    @OneToMany(
            mappedBy = "menu",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Plat> plats = new ArrayList<>();

}
