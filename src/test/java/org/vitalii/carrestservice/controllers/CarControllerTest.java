package org.vitalii.carrestservice.controllers;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.validation.BindException;
import org.vitalii.carrestservice.database.entities.CarYear;
import org.vitalii.carrestservice.dto.*;
import org.vitalii.carrestservice.dto.filters.CarFilter;
import org.vitalii.carrestservice.services.*;
import org.vitalii.carrestservice.services.validators.CarCreateEditDtoValidator;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(controllers = CarController.class)
@ActiveProfiles("noauth")
class CarControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CarService carService;
    @MockBean
    private CarBrandService carBrandService;
    @MockBean
    private CarModelService carModelService;
    @MockBean
    private CarYearService carYearService;
    @MockBean
    private CategoryService categoryService;
    @SpyBean
    private CarCreateEditDtoValidator carCreateEditDtoValidator;

    private final CarReadDto carReadDto = new CarReadDto(
            1,
            new CarBrandReadDto(1, "brand1"),
            new CarModelReadDto(1, "model1"),
            new CarYearReadDto(1, "1970"),
            List.of(new CategoryReadDto(1, "category1")));

    private final String carCreateUpdateJson = "{\"brand\":{\"brand\":\"brand1\"},\"model\":{\"model\":\"model1\"}," +
            "\"year\":{\"year\":\"1970\"},\"categories\":[{\"name\":\"category1\"}]}";

    @Test
    void findAllCars() throws Exception {
        Mockito.doReturn(new PageImpl<>(List.of(carReadDto))).when(carService)
                .findAll(Mockito.any(CarFilter.class), Mockito.any(Pageable.class));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/cars")
                .accept(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String excepted = "{\"content\":[{\"id\":1,\"brand\":{\"id\":1,\"brand\":\"brand1\"}," +
                "\"model\":{\"id\":1,\"model\":\"model1\"},\"year\":{\"id\":1,\"year\":\"1970\"}," +
                "\"categories\":[{\"id\":1,\"name\":\"category1\"}]}]," +
                "\"metadata\":{\"page\":0,\"size\":1,\"totalElements\":1}}";
        JSONAssert.assertEquals(excepted, result.getResponse().getContentAsString(), false);
    }

    @Test
    void findCarById() throws Exception {
        Mockito.doReturn(Optional.of(carReadDto)).when(carService).findById(Mockito.anyInt());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/cars/1")
                .accept(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String excepted = "{\"id\":1,\"brand\":{\"id\":1,\"brand\":\"brand1\"}," +
                "\"model\":{\"id\":1,\"model\":\"model1\"},\"year\":{\"id\":1,\"year\":\"1970\"}," +
                "\"categories\":[{\"id\":1,\"name\":\"category1\"}]}";
        JSONAssert.assertEquals(excepted, result.getResponse().getContentAsString(), false);
    }

    @Test
    void createCar() throws Exception {
        Mockito.doReturn(carReadDto).when(carService).save(Mockito.any(CarCreateEditDto.class));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v1/cars")
                .accept(MediaType.APPLICATION_JSON)
                .content(carCreateUpdateJson)
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String excepted = "{\"id\":1,\"brand\":{\"id\":1,\"brand\":\"brand1\"}," +
                "\"model\":{\"id\":1,\"model\":\"model1\"},\"year\":{\"id\":1,\"year\":\"1970\"}," +
                "\"categories\":[{\"id\":1,\"name\":\"category1\"}]}";
        assertEquals(HttpStatus.CREATED.value(), result.getResponse().getStatus());
        JSONAssert.assertEquals(excepted, result.getResponse().getContentAsString(), false);
    }

    @Test
    void updateCar() throws Exception {
        Mockito.doReturn(Optional.of(carReadDto)).when(carService).update(Mockito.anyInt(), Mockito.any(CarCreateEditDto.class));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/api/v1/cars/1")
                .accept(MediaType.APPLICATION_JSON)
                .content(carCreateUpdateJson)
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String excepted = "{\"id\":1,\"brand\":{\"id\":1,\"brand\":\"brand1\"}," +
                "\"model\":{\"id\":1,\"model\":\"model1\"},\"year\":{\"id\":1,\"year\":\"1970\"}," +
                "\"categories\":[{\"id\":1,\"name\":\"category1\"}]}";
        JSONAssert.assertEquals(excepted, result.getResponse().getContentAsString(), false);
    }

    @Test
    void deleteCar() throws Exception {
        Mockito.doReturn(true).when(carService).deleteById(Mockito.anyInt());
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/cars/1"))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    void createCarThrowBindException() throws Exception {
        Mockito.doReturn(Optional.of(new CarYear())).when(carYearService).findByYear(Mockito.anyString());
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/cars")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(carCreateUpdateJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof BindException));
    }

}