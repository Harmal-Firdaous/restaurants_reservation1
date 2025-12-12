package ma.emsi.userservice.mapper;

import ma.emsi.userservice.dto.UserDto;
import ma.emsi.userservice.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserDto toDto(User u) {
        if (u == null) return null;
        return UserDto.builder()
                .id(u.getId())
                .username(u.getUsername())
                .email(u.getEmail())
                .nom(u.getNom())
                .prenom(u.getPrenom())
                .roles(u.getRoles())
                .build();
    }
}
