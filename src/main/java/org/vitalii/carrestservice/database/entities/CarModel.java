package org.vitalii.carrestservice.database.entities;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "car_model")
public class CarModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "model", unique = true, nullable = false)
    private String model;

    @ToString.Exclude
    @Builder.Default
    @OneToMany(mappedBy = "model", fetch = FetchType.LAZY)
    private List<Car> cars = new ArrayList<>();

}
