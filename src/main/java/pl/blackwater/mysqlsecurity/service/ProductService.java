package pl.blackwater.mysqlsecurity.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.blackwater.mysqlsecurity.dto.ProductDTO;
import pl.blackwater.mysqlsecurity.dto.ProductDTOMapper;
import pl.blackwater.mysqlsecurity.model.Product;
import pl.blackwater.mysqlsecurity.model.ProductType;
import pl.blackwater.mysqlsecurity.repository.ProductRepository;

@RequiredArgsConstructor
@Service
public class ProductService {


    private final ProductRepository productRepository;



    //TODO CACHE ALL
    public ProductDTO createProduct(String title, String description, int price, ProductType type, String descriptionUrl){
        final Product product = new Product(title,description,price,type,descriptionUrl);
        productRepository.save(product);

        return ProductDTOMapper.fromProduct(product);
    }

}
