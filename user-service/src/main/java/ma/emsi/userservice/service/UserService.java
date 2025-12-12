package ma.emsi.userservice.service;

import ma.emsi.userservice.dto.RegisterRequest;
import ma.emsi.userservice.entity.User;

public interface UserService {
    User register(RegisterRequest request);
    User findByUsername(String username);
    User findById(Long id);
}
