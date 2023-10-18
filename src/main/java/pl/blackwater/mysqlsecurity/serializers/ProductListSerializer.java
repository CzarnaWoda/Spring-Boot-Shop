package pl.blackwater.mysqlsecurity.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.boot.jackson.JsonComponent;
import pl.blackwater.mysqlsecurity.model.Product;

import java.io.IOException;
import java.util.List;

@JsonComponent
public class ProductListSerializer extends JsonSerializer<List<Product>> {
    @Override
    public void serialize(List<Product> value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartArray();
        for(Product product : value){
            gen.writeObject(product);
        }
        gen.writeEndArray();
    }
}
