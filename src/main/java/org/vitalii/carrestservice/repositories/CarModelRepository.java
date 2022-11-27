package org.vitalii.carrestservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import org.vitalii.carrestservice.database.entities.CarModel;

import java.util.Optional;

@Repository
public interface CarModelRepository extends
        JpaRepository<CarModel, Integer>,
        QuerydslPredicateExecutor<CarModel> {

    Optional<CarModel> findByModel(String model);
}
