package pl.blackwater.mysqlsecurity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.blackwater.mysqlsecurity.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);
    User findByFirstName(String firstName);

    boolean existsUserByEmail(String email);
    boolean existsUserByFirstName(String firstName);



}
