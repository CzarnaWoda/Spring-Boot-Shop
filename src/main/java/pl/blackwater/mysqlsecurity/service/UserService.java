package pl.blackwater.mysqlsecurity.service;


import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.blackwater.mysqlsecurity.dto.UserDTO;
import pl.blackwater.mysqlsecurity.dto.UserDTOMapper;
import pl.blackwater.mysqlsecurity.model.User;
import pl.blackwater.mysqlsecurity.repository.RoleRepository;
import pl.blackwater.mysqlsecurity.repository.UserRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    //TODO CACHE ALL AND TRANSFORM ALL FROM CONTROLLERS TO SERVICE USERREPO ONLY HERE D:
    public UserDTO createUser(String firstName, String lastName, String email, String password, String phone) {
        final String passwordEncoded = passwordEncoder.encode(password);
        final User user = new User(firstName,lastName,email,passwordEncoded,phone,true,true,false, LocalDateTime.now());
        user.addRole(roleRepository.findByName("ROLE_USER"));
        userRepository.save(user);
        return UserDTOMapper.fromUser(user);
    }
}
