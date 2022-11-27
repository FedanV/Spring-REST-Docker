package org.vitalii.carrestservice.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.vitalii.carrestservice.database.entities.Category;
import org.vitalii.carrestservice.dto.CategoryCreateEditDto;
import org.vitalii.carrestservice.dto.CategoryReadDto;
import org.vitalii.carrestservice.dto.filters.CarFilter;

import java.util.Optional;

public interface CategoryService {

    Page<CategoryReadDto> finAll(CarFilter carFilter, Pageable pageable);

    Optional<CategoryReadDto> findById(Integer id);

    CategoryReadDto save(CategoryCreateEditDto categoryDto);

    Optional<CategoryReadDto> update(Integer id, CategoryCreateEditDto categoryDto);

    Boolean deleteById(Integer id);

    Optional<Category> findByName(String name);

}
