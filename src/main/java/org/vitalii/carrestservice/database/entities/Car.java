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
@Table(name = "car")
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "brand_id", referencedColumnName = "id")
    private CarBrand brand;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "year_id", referencedColumnName = "id")
    private CarYear year;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "model_id", referencedColumnName = "id")
    private CarModel model;

    @ToString.Exclude
    @Builder.Default
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
            name = "car_category",
            joinColumns = @JoinColumn(name = "car_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private List<Category> categories = new ArrayList<>();

    public void setBrand(CarBrand brand) {
        this.brand = brand;
        this.brand.getCars().add(this);
    }

    public void setYear(CarYear year) {
        this.year = year;
        this.year.getCars().add(this);
    }

    public void setModel(CarModel model) {
        this.model = model;
        this.model.getCars().add(this);
    }

}
