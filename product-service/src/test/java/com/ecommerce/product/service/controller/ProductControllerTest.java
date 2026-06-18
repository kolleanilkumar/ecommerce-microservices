package com.ecommerce.product.service.controller;

import com.ecommerce.product.service.dto.ProductDTO;
import com.ecommerce.product.service.service.ProductService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;

    @Test
    void testGetAllProducts() throws Exception {
        List<ProductDTO> products = Arrays.asList(
                new ProductDTO(1L, "Laptop", "Gaming Laptop", "Electronics", 50000.0, 10),
                new ProductDTO(2L, "Phone", "Smartphone", "Electronics", 20000.0, 5)
        );

        Mockito.when(productService.getAllProducts()).thenReturn(products);

        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(2)))
                .andExpect(jsonPath("$[0].name", is("Laptop")))
                .andExpect(jsonPath("$[1].name", is("Phone")));
    }

    @Test
    void testGetProductById() throws Exception {
        ProductDTO product = new ProductDTO(1L, "Laptop", "Gaming Laptop", "Electronics", 50000.0, 10);

        Mockito.when(productService.getProductById(1L)).thenReturn(product);

        mockMvc.perform(get("/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Laptop")))
                .andExpect(jsonPath("$.price", is(50000.0)));
    }

    @Test
    void testCreateProduct() throws Exception {
        ProductDTO product = new ProductDTO(1L, "Laptop", "Gaming Laptop", "Electronics", 50000.0, 10);

        Mockito.when(productService.createProduct(Mockito.any(ProductDTO.class))).thenReturn(product);

        String json = """
                {
                  "name": "Laptop",
                  "description": "Gaming Laptop",
                  "category": "Electronics",
                  "price": 50000.0,
                  "stockQuantity": 10
                }
                """;

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("Laptop")))
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    void testDeleteProduct() throws Exception {
        Mockito.when(productService.deleteProduct(1L)).thenReturn("Product ID 1 is Deleted Successfully");

        mockMvc.perform(delete("/products/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Product ID 1 is Deleted Successfully"));
    }
}
