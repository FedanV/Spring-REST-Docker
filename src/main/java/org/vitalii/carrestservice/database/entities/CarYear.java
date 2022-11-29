package org.vitalii.carrestservice.database.entities;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "car_year")
public class CarYear {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "year", unique = true, nullable = false)
    private String year;

    @ToString.Exclude
    @Builder.Default
    @OneToMany(mappedBy = "year", fetch = FetchType.LAZY)
    private List<Car> cars = new ArrayList<>();

}
