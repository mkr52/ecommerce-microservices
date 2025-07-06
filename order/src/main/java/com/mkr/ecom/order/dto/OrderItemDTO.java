package com.mkr.ecom.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class OrderItemDTO {
    private Long id;
    private String productId;
    private Integer quantity;
    private BigDecimal price;
    private BigDecimal subTotal;
}
