package pl.blackwater.mysqlsecurity.dto;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import pl.blackwater.mysqlsecurity.model.ProductType;

public class ProductDTO {

    private String title;
    private String description;
    private int price;
    private ProductType type;
    private String descriptionUrl;

    public ProductDTO(String title, String description, int price, ProductType type, String descriptionUrl) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.type = type;
        this.descriptionUrl = descriptionUrl;
    }

    public ProductDTO(){}
}
