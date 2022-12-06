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
import org.vitalii.carrestservice.database.entities.CarYear;
import org.vitalii.carrestservice.dto.CarYearCreateEditDto;
import org.vitalii.carrestservice.dto.CarYearReadDto;
import org.vitalii.carrestservice.dto.filters.CarFilter;
import org.vitalii.carrestservice.services.CarYearService;
import org.vitalii.carrestservice.services.validators.CarYearCreateEditDtoValidator;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(controllers = CarYearController.class)
@Import(SecurityConfig.class)
class CarYearControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CarYearService carYearService;
    @SpyBean
    private CarYearCreateEditDtoValidator carYearCreateEditDtoValidator;

    private final CarYearReadDto carYearReadDto1 = new CarYearReadDto(1, "1910");
    private final CarYearReadDto carYearReadDto2 = new CarYearReadDto(1, "1912");
    private final String carYearCreateUpdateJson = "{\"year\":\"1910\"}";

    @Test
    void findAllYears() throws Exception {
        Mockito.doReturn(new PageImpl<>(List.of(carYearReadDto1, carYearReadDto2))).when(carYearService)
                .findAll(Mockito.any(CarFilter.class), Mockito.any(Pageable.class));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/manufacturers/models/years")
                .accept(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String expected = "{\"content\":[{\"id\":1,\"year\":\"1910\"},{\"id\":1,\"year\":\"1912\"}]," +
                "\"metadata\":{\"page\":0,\"size\":2,\"totalElements\":2}}";
        JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
    }

    @Test
    void findYearById() throws Exception {
        Mockito.doReturn(Optional.of(carYearReadDto1)).when(carYearService).findById(Mockito.anyInt());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/manufacturers/models/years/1")
                .accept(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String expected = "{\"id\":1,\"year\":\"1910\"}";
        JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
    }

    @Test
    @WithMockUser
    void createYear() throws Exception {
        Mockito.doReturn(carYearReadDto1).when(carYearService).save(Mockito.any(CarYearCreateEditDto.class));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v1/manufacturers/models/years")
                .accept(MediaType.APPLICATION_JSON)
                .content(carYearCreateUpdateJson)
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String expected = "{\"id\":1,\"year\":\"1910\"}";
        assertEquals(HttpStatus.CREATED.value(), result.getResponse().getStatus());
        JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
    }

    @Test
    @WithMockUser
    void updateYear() throws Exception {
        Mockito.doReturn(Optional.of(carYearReadDto1)).when(carYearService).update(Mockito.anyInt(), Mockito.any(CarYearCreateEditDto.class));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/api/v1/manufacturers/models/years/1")
                .accept(MediaType.APPLICATION_JSON)
                .content(carYearCreateUpdateJson)
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String expected = "{\"id\":1,\"year\":\"1910\"}";
        JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
    }

    @Test
    @WithMockUser
    void deleteYear() throws Exception {
        Mockito.doReturn(true).when(carYearService).deleteById(Mockito.anyInt());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/v1/manufacturers/models/years/1");
        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @WithMockUser
    void createYearThrowBindException() throws Exception {
        Mockito.doReturn(Optional.of(new CarYear())).when(carYearService).findByYear(Mockito.anyString());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v1/manufacturers/models/years")
                .accept(MediaType.APPLICATION_JSON)
                .content(carYearCreateUpdateJson)
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof BindException));
    }

    @Test
    @WithMockUser
    void updateYearThrowBindException() throws Exception {
        Mockito.doReturn(Optional.of(new CarYear())).when(carYearService).findByYear(Mockito.anyString());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/api/v1/manufacturers/models/years/1")
                .accept(MediaType.APPLICATION_JSON)
                .content(carYearCreateUpdateJson)
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof BindException));
    }

}