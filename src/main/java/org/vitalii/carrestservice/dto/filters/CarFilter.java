package org.vitalii.carrestservice.dto.filters;

public record CarFilter(
        String manufacturer,
        String model,
        String minYear,
        String maxYear,
        String category) {
}
