package org.vitalii.carrestservice.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class CarYearCreateEditDto {
    @Min(value = 1900, message = "Year can't be less than 1900")
    @NotBlank(message = "Year is mandatory")
    private String year;
}
