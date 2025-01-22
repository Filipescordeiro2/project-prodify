package com.project.prodify.input;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {

    private Long id;
    @NotBlank(message = "Name is requeired")
    private String name;
    @NotBlank(message = "Price is requeired")
    private BigDecimal price;
    @NotBlank(message = "Stock is requeired")
    private int stock;
    @NotBlank(message = "Status is requeired")
    private boolean status;

}
