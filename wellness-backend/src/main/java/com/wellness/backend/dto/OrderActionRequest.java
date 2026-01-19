package com.wellness.backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderActionRequest {

    @NotBlank(message = "Reason is mandatory")
    private String reason;
}
