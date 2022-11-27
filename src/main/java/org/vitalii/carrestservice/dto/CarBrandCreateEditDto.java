package org.vitalii.carrestservice.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class CarBrandCreateEditDto {

    @NotBlank(message = "Brand is mandatory")
    private String brand;

}
