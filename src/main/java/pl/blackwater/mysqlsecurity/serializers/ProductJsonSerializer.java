package pl.blackwater.mysqlsecurity.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.boot.jackson.JsonComponent;
import pl.blackwater.mysqlsecurity.model.Product;

import java.io.IOException;

@JsonComponent
public class ProductJsonSerializer extends JsonSerializer<Product> {
    @Override
    public void serialize(Product value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        gen.writeStringField("title",value.getTitle());
        gen.writeStringField("description",value.getDescription());
        gen.writeNumberField("price",value.getPrice());
        gen.writeStringField("type",value.getType().name());
        gen.writeStringField("descriptionUrl",value.getDescriptionUrl());
        gen.writeEndObject();
    }
}
