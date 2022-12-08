package org.vitalii.carrestservice.dto;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CarReadDto {

    private Integer id;
    private CarBrandReadDto brand;
    private CarModelReadDto model;
    private CarYearReadDto year;
    private List<CategoryReadDto> categories;

}
