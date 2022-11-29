package org.vitalii.carrestservice.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class CategoryCreateEditDto {
    @NotBlank(message = "Name is mandatory")
    private String name;
}
