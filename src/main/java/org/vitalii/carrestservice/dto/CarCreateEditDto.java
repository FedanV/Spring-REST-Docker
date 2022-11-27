package org.vitalii.carrestservice.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CarCreateEditDto {
    CarBrandCreateEditDto brand;
    CarModelCreateEditDto model;
    CarYearCreateEditDto year;
    List<CategoryCreateEditDto> categories;
}
