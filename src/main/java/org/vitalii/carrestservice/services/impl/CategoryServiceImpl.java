package org.vitalii.carrestservice.services.impl;

import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.vitalii.carrestservice.database.entities.Category;
import org.vitalii.carrestservice.database.entities.QCategory;
import org.vitalii.carrestservice.database.querydsl.QPredicates;
import org.vitalii.carrestservice.dto.CategoryCreateEditDto;
import org.vitalii.carrestservice.dto.CategoryReadDto;
import org.vitalii.carrestservice.dto.filters.CarFilter;
import org.vitalii.carrestservice.repositories.CategoryRepository;
import org.vitalii.carrestservice.services.CategoryService;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<CategoryReadDto> finAll(CarFilter carFilter, Pageable pageable) {
        Predicate predicate = QPredicates.builder()
                .add(carFilter.category(), QCategory.category.name::eq)
                .buildAnd();
        return categoryRepository.findAll(predicate, pageable)
                .map(category -> modelMapper.map(category, CategoryReadDto.class));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CategoryReadDto> findById(Integer id) {
        return categoryRepository.findById(id)
                .map(category -> modelMapper.map(category, CategoryReadDto.class));
    }

    @Override
    @Transactional
    public CategoryReadDto save(CategoryCreateEditDto category) {
        return Optional.of(category)
                .map(categoryDto -> modelMapper.map(categoryDto, Category.class))
                .map(categoryRepository::save)
                .map(c -> modelMapper.map(c, CategoryReadDto.class))
                .orElseThrow();
    }

    @Override
    public Optional<CategoryReadDto> update(Integer id, CategoryCreateEditDto categoryDto) {
        return categoryRepository.findById(id)
                .map(category -> {
                    modelMapper.map(categoryDto, category);
                    return category;
                })
                .map(categoryRepository::saveAndFlush)
                .map(category -> modelMapper.map(category, CategoryReadDto.class));
    }

    @Override
    @Transactional
    public Boolean deleteById(Integer id) {
        return categoryRepository.findById(id)
                .map(category -> {
                    categoryRepository.delete(category);
                    categoryRepository.flush();
                    return true;
                })
                .orElse(false);
    }

    @Override
    public Optional<Category> findByName(String name) {
        return categoryRepository.findByName(name);
    }

}
