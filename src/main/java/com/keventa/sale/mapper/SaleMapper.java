package com.keventa.sale.mapper;

import com.keventa.sale.dto.SaleResponse;
import com.keventa.sale.entity.Sale;
import com.keventa.sale.enums.SaleStatus;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Component
public class SaleMapper {

    public SaleResponse toResponse(Sale sale){
        if(sale==null){
            throw new IllegalArgumentException("Mapeo de venta vacio. No posible");
        }

        Long id = sale.getId();
        Long productId = sale.getProductId();
        String productName = sale.getProductName();
        BigDecimal purchasePrice = sale.getPurchasePrice();
        BigDecimal salePrice = sale.getSalePrice();
        Integer quantity = sale.getQuantity();
        BigDecimal total = sale.getTotal();
        BigDecimal profit = sale.getProfit();
        LocalDateTime saleDate = sale.getSaleDate();
        SaleStatus status = sale.getStatus();

        return new SaleResponse(id,
                productId,
                productName,
                purchasePrice,
                salePrice,
                quantity,
                total,
                profit,
                saleDate,
                status);

    }
}
