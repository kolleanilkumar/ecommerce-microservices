package com.ecommerce.product.service.service;

import com.ecommerce.product.service.dto.ProductDTO;
import com.ecommerce.product.service.entity.Product;
import com.ecommerce.product.service.exception.ProductNotFoundException;
import com.ecommerce.product.service.mapper.ProductMapper;
import com.ecommerce.product.service.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductServiceImplementationTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductServiceImplementation productService;

    @Test
    void shouldReturnAllProducts() {

        Product p1 = new Product();
        p1.setId(1L);
        p1.setName("Laptop");

        Product p2 = new Product();
        p2.setId(2L);
        p2.setName("Phone");

        ProductDTO dto1 = new ProductDTO();
        dto1.setId(1L);
        dto1.setName("Laptop");

        ProductDTO dto2 = new ProductDTO();
        dto2.setId(2L);
        dto2.setName("Phone");

        when(productRepository.findAll())
                .thenReturn(List.of(p1, p2));

        when(productMapper.toDTOList(anyList()))
                .thenReturn(List.of(dto1, dto2));

        List<ProductDTO> products =
                productService.getAllProducts();

        assertEquals(2, products.size());
    }

    @Test
    void shouldReturnProductById() {

        Product product = new Product();
        product.setId(1L);
        product.setName("Laptop");

        ProductDTO dto = new ProductDTO();
        dto.setId(1L);
        dto.setName("Laptop");

        when(productRepository.findById(1L))
                .thenReturn(Optional.of(product));

        when(productMapper.toDTO(product))
                .thenReturn(dto);

        ProductDTO result =
                productService.getProductById(1L);

        assertEquals("Laptop",
                result.getName());
    }

    @Test
    void shouldThrowProductNotFoundException() {

        when(productRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                ProductNotFoundException.class,
                () -> productService.getProductById(1L)
        );
    }

    @Test
    void shouldCreateProduct() {

        ProductDTO dto = new ProductDTO();

        dto.setName("Laptop");
        dto.setPrice(1000.0);

        Product product = new Product();

        product.setName("Laptop");
        product.setPrice(1000.0);

        when(productMapper.toEntity(dto))
                .thenReturn(product);

        when(productRepository.save(product))
                .thenReturn(product);

        when(productMapper.toDTO(product))
                .thenReturn(dto);

        ProductDTO saved =
                productService.createProduct(dto);

        assertEquals(
                "Laptop",
                saved.getName());
    }

    @Test
    void shouldDeleteProduct() {

        when(productRepository.existsById(1L))
                .thenReturn(true);

        String result =
                productService.deleteProduct(1L);

        verify(productRepository)
                .deleteById(1L);

        assertEquals(
                "Product ID 1 is Deleted Successfully",
                result);
    }

    @Test
    void shouldThrowExceptionWhenDeleteProductNotFound() {

        when(productRepository.existsById(1L))
                .thenReturn(false);

        assertThrows(
                ProductNotFoundException.class,
                () -> productService.deleteProduct(1L)
        );
    }
}
