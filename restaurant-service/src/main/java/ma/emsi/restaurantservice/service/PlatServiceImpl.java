package ma.emsi.restaurantservice.service;

import lombok.RequiredArgsConstructor;
import ma.emsi.restaurantservice.dto.PlatDto;
import ma.emsi.restaurantservice.entity.Menu;
import ma.emsi.restaurantservice.entity.Plat;
import ma.emsi.restaurantservice.mapper.PlatMapper;
import ma.emsi.restaurantservice.repository.MenuRepository;
import ma.emsi.restaurantservice.repository.PlatRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlatServiceImpl implements PlatService {

    private final PlatRepository platRepository;
    private final MenuRepository menuRepository;
    private final PlatMapper mapper;

    @Override
    @Transactional
    public PlatDto create(Long menuId, PlatDto dto) {
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new RuntimeException("Menu not found with id: " + menuId));

        Plat plat = mapper.toEntity(dto);
        plat.setMenu(menu);

        Plat saved = platRepository.save(plat);
        return mapper.toDto(saved);
    }

    @Override
    @Transactional
    public PlatDto update(Long id, PlatDto dto) {
        Plat plat = platRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Plat not found with id: " + id));

        plat.setName(dto.getName());
        plat.setDescription(dto.getDescription());
        plat.setPrice(dto.getPrice());

        Plat updated = platRepository.save(plat);
        return mapper.toDto(updated);
    }

    @Override
    public void delete(Long id) {
        if (!platRepository.existsById(id)) {
            throw new RuntimeException("Plat not found with id: " + id);
        }
        platRepository.deleteById(id);
    }

    @Override
    public PlatDto getById(Long id) {
        Plat plat = platRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Plat not found with id: " + id));
        return mapper.toDto(plat);
    }

    @Override
    public List<PlatDto> getByMenu(Long menuId) {
        return platRepository.findByMenuId(menuId).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }
}