package com.mkr.ecom.order.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItemRequest {
    private String productId;
    private Integer quantity;
}
