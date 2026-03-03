package uz.uzumtech.retail_service.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import uz.uzumtech.retail_service.dto.response.CategoryResponse;
import uz.uzumtech.retail_service.dto.response.PageResponse;
import uz.uzumtech.retail_service.service.CategoryService;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CategoryController.class)
public class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CategoryService categoryService;

    @Test
    @DisplayName("getAllCategories - Успешно")
    void getAllCategories_ShouldReturnListOfCategories() throws Exception {
        var category1 = new CategoryResponse(1L, "Category1", "Category1");
        var category2 = new CategoryResponse(2L, "Category2", "Category2");
        var category3 = new CategoryResponse(3L, "Category3", "Category3");

        var categories = List.of(category1, category2, category3);
        var expectedResponse = new PageResponse<>(categories, 3, 1, 1, true);

        when(categoryService.getAll(0, 10)).thenReturn(expectedResponse);

        mockMvc.perform(get("/api/v1/retail/categories")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(3)))
                .andExpect(jsonPath("$.total_elements").value(3))
                .andExpect(jsonPath("$.is_last").value(true));

        Mockito.verify(categoryService, Mockito.times(1)).getAll(0, 10);
    }

    @Test
    @DisplayName("getAllCategories - Пустой лист")
    void getAllCategories_ShouldReturnEmptyPage_WhenNoCategoriesExist() throws Exception {
        var expectedResponse = new PageResponse<CategoryResponse>(List.of(), 0, 0, 0, true);

        when(categoryService.getAll(0, 10)).thenReturn(expectedResponse);

        mockMvc.perform(get("/api/v1/retail/categories")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isEmpty())
                .andExpect(jsonPath("$.total_elements").value(0));
    }

    @Test
    @DisplayName("getAllCategories - Запрос без параметров")
    void getAllCategories_ShouldReturnBadRequest_WhenParamsMissing() throws Exception {
        mockMvc.perform(get("/api/v1/retail/categories"))
                .andExpect(status().isOk());
    }
}
