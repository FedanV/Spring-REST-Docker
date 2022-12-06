package org.vitalii.carrestservice.controllers;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.validation.BindException;
import org.vitalii.carrestservice.configurations.SecurityConfig;
import org.vitalii.carrestservice.database.entities.CarBrand;
import org.vitalii.carrestservice.dto.CarBrandCreateEditDto;
import org.vitalii.carrestservice.dto.CarBrandReadDto;
import org.vitalii.carrestservice.dto.filters.CarFilter;
import org.vitalii.carrestservice.services.CarBrandService;
import org.vitalii.carrestservice.services.validators.CarBrandCreateEditDtoValidator;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(controllers = CarBrandController.class)
@Import(SecurityConfig.class)
class CarBrandControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CarBrandService carBrandService;

    @SpyBean
    private CarBrandCreateEditDtoValidator carBrandCreateEditDtoValidator;

    private final CarBrandReadDto mockBrandReadDto1 = new CarBrandReadDto(1, "brand1");
    private final CarBrandReadDto mockBrandReadDto2 = new CarBrandReadDto(2, "brand2");
    private final String brandCreateEditJson = "{\"brand\":\"brand1\"}";

    @Test
    void findAllManufactures() throws Exception {
        Mockito.doReturn(new PageImpl<>(List.of(mockBrandReadDto1, mockBrandReadDto2)))
                .when(carBrandService).findAll(Mockito.any(CarFilter.class), Mockito.any(Pageable.class));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/manufacturers")
                .accept(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String expected = "{\"content\":[{\"id\":1,\"brand\":\"brand1\"},{\"id\":2,\"brand\":\"brand2\"}]," +
                "\"metadata\":{\"page\":0,\"size\":2,\"totalElements\":2}}";
        JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
    }

    @Test
    void findManufactureById() throws Exception {
        Mockito.doReturn(Optional.of(mockBrandReadDto1)).when(carBrandService).findById(Mockito.anyInt());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/manufacturers/1")
                .accept(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String expected = "{\"id\":1,\"brand\":\"brand1\"}";
        JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
    }

    @Test
    @WithMockUser
    void createManufacture() throws Exception {
        Mockito.doReturn(mockBrandReadDto1).when(carBrandService).save(Mockito.any(CarBrandCreateEditDto.class));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v1/manufacturers")
                .accept(MediaType.APPLICATION_JSON)
                .content(brandCreateEditJson)
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        assertEquals(HttpStatus.CREATED.value(), result.getResponse().getStatus());
        String expected = "{\"id\":1,\"brand\":\"brand1\"}";
        JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
    }

    @Test
    @WithMockUser
    void updateManufacture() throws Exception {
        Mockito.doReturn(Optional.of(mockBrandReadDto1)).when(carBrandService).update(Mockito.anyInt(), Mockito.any(CarBrandCreateEditDto.class));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/api/v1/manufacturers/1")
                .accept(MediaType.APPLICATION_JSON)
                .content(brandCreateEditJson)
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String expected = "{\"id\":1,\"brand\":\"brand1\"}";
        JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
    }

    @Test
    @WithMockUser
    void deleteManufacture() throws Exception {
        Mockito.doReturn(true).when(carBrandService).deleteById(Mockito.anyInt());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/v1/manufacturers/1");
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        assertEquals(HttpStatus.NO_CONTENT.value(), result.getResponse().getStatus());
    }

    @Test
    @WithMockUser
    void createManufactureThrowBindingException() throws Exception {
        Mockito.doReturn(Optional.of(new CarBrand())).when(carBrandService).findByBrand(Mockito.anyString());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v1/manufacturers")
                .accept(MediaType.APPLICATION_JSON)
                .content(brandCreateEditJson)
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof BindException));

    }

    @Test
    @WithMockUser
    void updateManufactureThrowBindingException() throws Exception {
        Mockito.doReturn(Optional.of(new CarBrand())).when(carBrandService).findByBrand(Mockito.anyString());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/api/v1/manufacturers/1")
                .accept(MediaType.APPLICATION_JSON)
                .content(brandCreateEditJson)
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof BindException));
    }

}