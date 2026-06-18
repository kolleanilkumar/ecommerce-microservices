package com.ecommerce.order.service.client;

import com.ecommerce.order.service.dto.ProductResponse;
import org.springframework.stereotype.Component;

@Component
public class ProductClientFallback implements ProductClient {

    @Override
    public ProductResponse getProductById(Long id) {

        throw new RuntimeException(
                "Product Service is unavailable, Unable to Place Order"
        );
    }
}
