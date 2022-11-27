package org.vitalii.carrestservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CarReadDto {

    private Integer id;
    private CarBrandReadDto brand;
    private CarModelReadDto model;
    private CarYearReadDto year;
    private List<CategoryReadDto> categories;

}
