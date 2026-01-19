package com.wellness.backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UpdateCartRequest {
    private Long productId;
    private int quantity;
}