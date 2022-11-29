package org.vitalii.carrestservice.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class CarModelCreateEditDto {
    @NotBlank(message = "Model is mandatory")
    private String model;
}
