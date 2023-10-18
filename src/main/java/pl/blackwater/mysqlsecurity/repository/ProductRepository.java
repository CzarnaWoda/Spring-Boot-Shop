package pl.blackwater.mysqlsecurity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.blackwater.mysqlsecurity.model.Product;
import pl.blackwater.mysqlsecurity.model.ProductType;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ProductRepository extends JpaRepository<Product,Long> {


    @Override
    List<Product> findAll();

    Set<Product> findAllByType(ProductType type);

    Optional<Product> findById(Long id);
}
