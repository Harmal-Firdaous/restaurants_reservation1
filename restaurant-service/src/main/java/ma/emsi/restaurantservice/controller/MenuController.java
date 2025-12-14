package ma.emsi.restaurantservice.controller;

import lombok.RequiredArgsConstructor;
import ma.emsi.restaurantservice.dto.MenuDto;
import ma.emsi.restaurantservice.service.MenuService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menus")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    @PostMapping("/restaurant/{restaurantId}")
    public ResponseEntity<MenuDto> create(
            @PathVariable Long restaurantId,
            @RequestBody MenuDto dto
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(menuService.create(restaurantId, dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MenuDto> update(
            @PathVariable Long id,
            @RequestBody MenuDto dto
    ) {
        return ResponseEntity.ok(menuService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        menuService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<List<MenuDto>> getByRestaurant(
            @PathVariable Long restaurantId
    ) {
        return ResponseEntity.ok(menuService.getByRestaurant(restaurantId));
    }
}
