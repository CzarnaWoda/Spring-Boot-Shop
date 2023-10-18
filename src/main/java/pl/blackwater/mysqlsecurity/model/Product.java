package pl.blackwater.mysqlsecurity.model;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@NoArgsConstructor
@Entity
@Getter
@JsonInclude
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;
    private String description;
    private int price;
    private ProductType type;
    private String descriptionUrl;

    public Product(Long id, String title, String description, int price, ProductType type, String descriptionUrl) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.price = price;
        this.type = type;
        this.descriptionUrl = descriptionUrl;
    }

    public Product(String title, String description, int price, ProductType type, String descriptionUrl) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.type = type;
        this.descriptionUrl = descriptionUrl;
    }
}
