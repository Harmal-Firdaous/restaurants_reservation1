package ma.emsi.userservice.controller;

import lombok.RequiredArgsConstructor;
import ma.emsi.userservice.dto.UserDto;
import ma.emsi.userservice.entity.User;
import ma.emsi.userservice.mapper.UserMapper;
import ma.emsi.userservice.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper mapper;

    // Get public info about user (used by other services)
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable Long id) {
        User u = userService.findById(id);
        return ResponseEntity.ok(mapper.toDto(u));
    }
}
