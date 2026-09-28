package com.keventa.product.mapper;

import com.keventa.product.dto.CreateProductRequest;
import com.keventa.product.dto.ProductResponse;
import com.keventa.product.entity.Product;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class ProductMapper {

    public Product toEntity(CreateProductRequest request){
        if(request==null){
            throw new IllegalArgumentException("Solicitud de creacion de producto vacia");
        }

        Product product = new Product();

        product.setName(request.name());
        product.setPurchasePrice(request.purchasePrice());
        product.setSalePrice(request.salePrice());
        product.setStock(request.stock());

        return product;
    }

    public ProductResponse toResponse(Product product){
        if(product==null){
            throw new IllegalArgumentException("Mapeo de producto vacio no posible");
        }

        Long id = product.getId();
        String name = product.getName();
        BigDecimal purchasePrice = product.getPurchasePrice();
        BigDecimal salePrice = product.getSalePrice();
        Integer stock = product.getStock();

        return new ProductResponse(id, name, purchasePrice, salePrice, stock);

    }
}
