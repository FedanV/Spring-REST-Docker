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
import org.vitalii.carrestservice.database.entities.CarModel;
import org.vitalii.carrestservice.dto.CarModelCreateEditDto;
import org.vitalii.carrestservice.dto.CarModelReadDto;
import org.vitalii.carrestservice.dto.filters.CarFilter;
import org.vitalii.carrestservice.services.CarModelService;
import org.vitalii.carrestservice.services.validators.CarModelCreateEditDtoValidator;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(controllers = CarModelController.class)
@ActiveProfiles("noauth")
class CarModelControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CarModelService carModelService;
    @SpyBean
    private CarModelCreateEditDtoValidator carModelCreateEditDtoValidator;

    private final CarModelReadDto carModelReadDto1 = new CarModelReadDto(1, "model1");
    private final CarModelReadDto carModelReadDto2 = new CarModelReadDto(2, "model2");
    private final String carModelCreateUpdateJson = "{\"model\":\"model1\"}";

    @Test
    void findAllModels() throws Exception {
        Mockito.doReturn(new PageImpl<>(List.of(carModelReadDto1, carModelReadDto2))).when(carModelService)
                .findAll(Mockito.any(CarFilter.class), Mockito.any(Pageable.class));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/manufacturers/models")
                .accept(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String expected = "{\"content\":[{\"id\":1,\"model\":\"model1\"},{\"id\":2,\"model\":\"model2\"}]," +
                "\"metadata\":{\"page\":0,\"size\":2,\"totalElements\":2}}";
        JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
    }

    @Test
    void findModelById() throws Exception {
        Mockito.doReturn(Optional.of(carModelReadDto1)).when(carModelService).findById(Mockito.anyInt());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/manufacturers/models/1")
                .accept(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String expected = "{\"id\":1,\"model\":\"model1\"}";
        JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
    }

    @Test
    void createModel() throws Exception {
        Mockito.doReturn(carModelReadDto1).when(carModelService).save(Mockito.any(CarModelCreateEditDto.class));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v1/manufacturers/models")
                .accept(MediaType.APPLICATION_JSON)
                .content(carModelCreateUpdateJson)
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String expected = "{\"id\":1,\"model\":\"model1\"}";
        assertEquals(HttpStatus.CREATED.value(), result.getResponse().getStatus());
        JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
    }

    @Test
    void updateModel() throws Exception {
        Mockito.doReturn(Optional.of(carModelReadDto1)).when(carModelService).update(Mockito.anyInt(), Mockito.any(CarModelCreateEditDto.class));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/api/v1/manufacturers/models/1")
                .accept(MediaType.APPLICATION_JSON)
                .content(carModelCreateUpdateJson)
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String expected = "{\"id\":1,\"model\":\"model1\"}";
        JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
    }

    @Test
    void deleteModel() throws Exception {
        Mockito.doReturn(true).when(carModelService).deleteById(Mockito.anyInt());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/v1/manufacturers/models/1");
        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    void createModelThrowBindException() throws Exception {
        Mockito.doReturn(Optional.of(new CarModel())).when(carModelService).findByModel(Mockito.anyString());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v1/manufacturers/models")
                .accept(MediaType.APPLICATION_JSON)
                .content(carModelCreateUpdateJson)
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof BindException));
    }

    @Test
    void updateModelThrowBindException() throws Exception {
        Mockito.doReturn(Optional.of(new CarModel())).when(carModelService).findByModel(Mockito.anyString());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/api/v1/manufacturers/models/1")
                .accept(MediaType.APPLICATION_JSON)
                .content(carModelCreateUpdateJson)
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof BindException));
    }

}