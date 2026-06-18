package com.ecommerce.product.service.service;

import com.ecommerce.product.service.dto.ProductDTO;

import java.util.List;

public interface ProductService {

    List<ProductDTO> getAllProducts();

    ProductDTO getProductById(Long id);

    ProductDTO createProduct(ProductDTO productDTO);

    String deleteProduct(Long id);
}
