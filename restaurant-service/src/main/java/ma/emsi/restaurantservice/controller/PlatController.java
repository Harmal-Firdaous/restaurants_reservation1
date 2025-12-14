package ma.emsi.restaurantservice.controller;

import lombok.RequiredArgsConstructor;
import ma.emsi.restaurantservice.dto.PlatDto;
import ma.emsi.restaurantservice.service.PlatService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/plats")
@RequiredArgsConstructor
public class PlatController {

    private final PlatService platService;

    @PostMapping("/menu/{menuId}")
    public ResponseEntity<PlatDto> create(
            @PathVariable Long menuId,
            @RequestBody PlatDto dto
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(platService.create(menuId, dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlatDto> get(@PathVariable Long id) {
        return ResponseEntity.ok(platService.getById(id));
    }

    @GetMapping("/menu/{menuId}")
    public ResponseEntity<List<PlatDto>> getByMenu(@PathVariable Long menuId) {
        return ResponseEntity.ok(platService.getByMenu(menuId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PlatDto> update(
            @PathVariable Long id,
            @RequestBody PlatDto dto
    ) {
        return ResponseEntity.ok(platService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        platService.delete(id);
        return ResponseEntity.noContent().build();
    }
}