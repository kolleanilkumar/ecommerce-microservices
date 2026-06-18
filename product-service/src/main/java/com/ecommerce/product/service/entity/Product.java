package com.ecommerce.product.service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "products")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;                  // Unique identifier
    @Column(nullable = false)
    private String name;          // Product name
    private String description;   // Detailed description

    @Column(nullable = false)
    private String category;      // Category (e.g., Electronics, Clothing)

    @Column(nullable = false)
    private Double price;         // Unit price

    @Column(nullable = false)
    private Integer stockQuantity;// Available stock
}
