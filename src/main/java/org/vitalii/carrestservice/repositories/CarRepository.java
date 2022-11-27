package org.vitalii.carrestservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import org.vitalii.carrestservice.database.entities.Car;

@Repository
public interface CarRepository extends
        JpaRepository<Car, Integer>,
        QuerydslPredicateExecutor<Car> {
}
