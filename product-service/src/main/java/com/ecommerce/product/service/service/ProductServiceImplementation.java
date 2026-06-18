package com.ecommerce.product.service.service;

import com.ecommerce.product.service.dto.ProductDTO;
import com.ecommerce.product.service.entity.Product;
import com.ecommerce.product.service.exception.ProductNotFoundException;
import com.ecommerce.product.service.mapper.ProductMapper;
import com.ecommerce.product.service.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImplementation implements ProductService {


    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductServiceImplementation(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }


    @Override
    public List<ProductDTO> getAllProducts() {
        return productMapper.toDTOList(productRepository.findAll());
    }

    @Override
    public ProductDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id " + id));
        return productMapper.toDTO(product);
    }

    @Override
    public ProductDTO createProduct(ProductDTO productDTO) {
        Product product = productMapper.toEntity(productDTO);
        Product saved = productRepository.save(product);
        return productMapper.toDTO(saved);
    }

    @Override
    public String deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ProductNotFoundException("Cannot delete. Product not found with id " + id);
        }
        productRepository.deleteById(id);

        return "Product ID " + id + " is Deleted Successfully";
    }
}
