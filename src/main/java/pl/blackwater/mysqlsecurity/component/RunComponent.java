package pl.blackwater.mysqlsecurity.component;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pl.blackwater.mysqlsecurity.model.Product;
import pl.blackwater.mysqlsecurity.model.ProductType;
import pl.blackwater.mysqlsecurity.model.Role;
import pl.blackwater.mysqlsecurity.model.User;
import pl.blackwater.mysqlsecurity.repository.ProductRepository;
import pl.blackwater.mysqlsecurity.repository.RoleRepository;
import pl.blackwater.mysqlsecurity.repository.UserRepository;
import pl.blackwater.mysqlsecurity.service.ProductService;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class RunComponent implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final ProductRepository productRepository;

    private final ProductService productService;


    @Override
    public void run(String... args) {
        if(roleRepository.findByName("ROLE_USER") == null) {
            roleRepository.save(new Role("ROLE_USER", "READ:USER,READ:CUSTOMER"));
            roleRepository.save(new Role("ROLE_ADMIN", "READ:USER,READ:CUSTOMER,READ:ADMIN"));
        }
        if(!userRepository.existsUserByFirstName("admin")){
            User user = new User("admin", "czarnawoda", "kmimat299@gmail.com", passwordEncoder.encode( "8655487154"), "517789666", true, true, false, LocalDateTime.now());
            user.addRole(roleRepository.findByName("ROLE_ADMIN"));

            userRepository.save(user);
        }
        if(productRepository.findAll().size() == 0){
            productService.createProduct("2","312d2",300, ProductType.ELECTRONICS,"123.pl");
            productService.createProduct("1","2dsaa",300, ProductType.ELECTRONICS,"123.pl");
            productService.createProduct("3","3dfssa2",300, ProductType.BAGS,"123.pl");
            productService.createProduct("4","2322",300, ProductType.BAGS,"123.pl");
            productService.createProduct("5","2dasdas",300, ProductType.ELECTRONICS,"123.pl");

        }
    }
}
