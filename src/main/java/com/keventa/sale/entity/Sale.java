package com.keventa.sale.entity;

import com.keventa.sale.enums.SaleStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "sales")
public class Sale {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "sale_seq")
    @SequenceGenerator(name = "sale_seq",
            sequenceName = "sale_seq",
            allocationSize = 50
    )
    private Long id;

    @NotNull
    @Positive
    @Column(name = "product_id")
    private Long productId;

    @NotBlank
    @Column(name = "product_name")
    private String productName;

    @NotNull
    @Positive
    @Column(name = "purchase_price")
    private BigDecimal purchasePrice;

    @NotNull
    @Positive
    @Column(name = "sale_price")
    private BigDecimal salePrice;

    @NotNull
    @Positive
    private Integer quantity;

    @NotNull
    @Positive
    private BigDecimal total;

    @NotNull
    @PositiveOrZero
    private BigDecimal profit;

    @NotNull
    @Column(name = "sale_date")
    private LocalDateTime saleDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    private SaleStatus status;
}
