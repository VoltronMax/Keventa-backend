package com.keventa.product.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "product_seq")
    @SequenceGenerator(name = "product_seq",
            sequenceName = "product_seq",
            allocationSize = 50
    )
    private Long id;

    @Version
    private Long version;

    @NotBlank
    @Column(unique = true, nullable = false)
    private String name;

    @NotNull
    @Positive
    @Column(name = "purchase_price")
    private BigDecimal purchasePrice;

    @NotNull
    @Positive
    @Column(name = "sale_price")
    private BigDecimal salePrice;

    @NotNull
    @PositiveOrZero
    private Integer stock;
}
