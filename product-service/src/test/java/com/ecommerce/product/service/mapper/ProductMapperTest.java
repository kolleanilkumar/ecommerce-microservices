package com.ecommerce.product.service.mapper;

import com.ecommerce.product.service.dto.ProductDTO;
import com.ecommerce.product.service.entity.Product;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProductMapperTest {

    private ProductMapper mapper = new ProductMapper();

    @Test
    void shouldConvertEntityToDTO() {

        Product product = new Product();

        product.setId(1L);
        product.setName("Laptop");

        ProductDTO dto = mapper.toDTO(product);

        assertEquals(
                product.getName(),
                dto.getName());
    }

    @Test
    void shouldConvertDTOToEntity() {

        ProductDTO dto = new ProductDTO();

        dto.setId(1L);
        dto.setName("Laptop");

        Product product =
                mapper.toEntity(dto);

        assertEquals(
                dto.getName(),
                product.getName());
    }
}
