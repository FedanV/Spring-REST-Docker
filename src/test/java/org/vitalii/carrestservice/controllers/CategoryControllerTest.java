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
import org.vitalii.carrestservice.configurations.SecurityConfig;
import org.vitalii.carrestservice.dto.CategoryCreateEditDto;
import org.vitalii.carrestservice.dto.CategoryReadDto;
import org.vitalii.carrestservice.dto.filters.CarFilter;
import org.vitalii.carrestservice.services.CategoryService;
import org.vitalii.carrestservice.services.validators.CategoryCreateEditDtoValidator;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(controllers = CategoryController.class)
@Import(SecurityConfig.class)
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;
    @SpyBean
    private CategoryCreateEditDtoValidator categoryCreateEditDtoValidator;

    private final CategoryReadDto categoryReadDto1 = new CategoryReadDto(1, "category1");
    private final CategoryReadDto categoryReadDto2 = new CategoryReadDto(2, "category1");
    private final String categoryCreateUpdateJson = "{\"name\":\"category1\"}";

    @Test
    void findAllCategories() throws Exception {
        Mockito.doReturn(new PageImpl<>(List.of(categoryReadDto1, categoryReadDto2))).when(categoryService)
                .finAll(Mockito.any(CarFilter.class), Mockito.any(Pageable.class));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/manufacturers/models/years/categories")
                .accept(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        String expected = "{\"content\":[{\"id\":1,\"name\":\"category1\"},{\"id\":2,\"name\":\"category1\"}]," +
                "\"metadata\":{\"page\":0,\"size\":2,\"totalElements\":2}}";
        JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
    }

    @Test
    void findCategoryById() throws Exception {
        Mockito.doReturn(Optional.of(categoryReadDto1)).when(categoryService).findById(Mockito.anyInt());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/manufacturers/models/years/categories/1")
                .accept(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        String expected = "{\"id\":1,\"name\":\"category1\"}";
        JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
    }

    @Test
    @WithMockUser
    void createCategory() throws Exception {
        Mockito.doReturn(categoryReadDto1).when(categoryService).save(Mockito.any(CategoryCreateEditDto.class));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v1/manufacturers/models/years/categories")
                .accept(MediaType.APPLICATION_JSON)
                .content(categoryCreateUpdateJson)
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();
        String expected = "{\"id\":1,\"name\":\"category1\"}";
        JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
    }

    @Test
    @WithMockUser
    void updateCategory() throws Exception {
        Mockito.doReturn(Optional.of(categoryReadDto1)).when(categoryService).update(Mockito.anyInt(), Mockito.any(CategoryCreateEditDto.class));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/api/v1/manufacturers/models/years/categories/1")
                .accept(MediaType.APPLICATION_JSON)
                .content(categoryCreateUpdateJson)
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        String expected = "{\"id\":1,\"name\":\"category1\"}";
        JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
    }

    @Test
    @WithMockUser
    void deleteCategory() throws Exception {
        Mockito.doReturn(true).when(categoryService).deleteById(Mockito.anyInt());
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/manufacturers/models/years/categories/1"))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @WithMockUser
    void deleteCategoryThrowNotFoundStatus() throws Exception {
        Mockito.doReturn(false).when(categoryService).deleteById(Mockito.anyInt());
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/manufacturers/models/years/categories/1"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

}