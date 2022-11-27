package org.vitalii.carrestservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import org.vitalii.carrestservice.database.entities.CarBrand;

import java.util.Optional;

@Repository
public interface CarBrandRepository extends
        JpaRepository<CarBrand, Integer>,
        QuerydslPredicateExecutor<CarBrand> {

    Optional<CarBrand> findByBrand(String brand);

}
