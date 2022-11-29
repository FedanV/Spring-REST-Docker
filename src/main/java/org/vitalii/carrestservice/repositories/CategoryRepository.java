package org.vitalii.carrestservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import org.vitalii.carrestservice.database.entities.Category;

import java.util.Optional;

@Repository
public interface CategoryRepository extends
        JpaRepository<Category, Integer>,
        QuerydslPredicateExecutor<Category> {
    Optional<Category> findByName(String name);
}
