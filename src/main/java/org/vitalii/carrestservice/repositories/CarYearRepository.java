package org.vitalii.carrestservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import org.vitalii.carrestservice.database.entities.CarYear;

import java.util.Optional;

@Repository
public interface CarYearRepository extends
        JpaRepository<CarYear, Integer>,
        QuerydslPredicateExecutor<CarYear> {
    Optional<CarYear> findByYear(String year);
}
