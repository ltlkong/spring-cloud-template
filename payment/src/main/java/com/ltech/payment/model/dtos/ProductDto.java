package com.ltech.payment.model.dtos;

import com.sun.istack.NotNull;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@Data
public class ProductDto implements Serializable {
    @NotNull
    private String customerId;
    @NotNull
    private String name;
    private String description;
    @NotNull
    private Long price;
    @NotNull
    private Long quantity;
    @NotNull
    private String currency;
    private String successUrl;
    private String cancelUrl;
    private List<String> imagesUrl;

}
