package pl.blackwater.mysqlsecurity.dto;

import org.springframework.beans.BeanUtils;
import org.springframework.security.core.parameters.P;
import pl.blackwater.mysqlsecurity.model.Product;

public class ProductDTOMapper {

    public static ProductDTO fromProduct(Product product){
        final ProductDTO productDTO = new ProductDTO();

        BeanUtils.copyProperties(product,productDTO);

        return productDTO;
    }
}
