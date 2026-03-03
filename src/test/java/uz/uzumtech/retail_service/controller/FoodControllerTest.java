package uz.uzumtech.retail_service.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import uz.uzumtech.retail_service.dto.response.FoodDetailsResponse;
import uz.uzumtech.retail_service.dto.response.FoodResponse;
import uz.uzumtech.retail_service.dto.response.PageResponse;
import uz.uzumtech.retail_service.dto.response.ReceiptItemResponse;
import uz.uzumtech.retail_service.service.FoodService;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FoodController.class)
public class FoodControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FoodService foodService;

    private final String BASE_URL = "/api/v1/retail/foods";

    @Test
    @DisplayName("getFoodsByCategoryId - Получение списка еды по категории")
    void getFoodsByCategoryId_ShouldReturnPageResponse() throws Exception {
        Long categoryId = 1L;
        int page = 0;
        int size = 10;

        var foodResponse = new FoodResponse(1L, "Плов");
        var expectedResponse = new PageResponse<>(List.of(foodResponse), 1, 1, 0, true);

        when(foodService.getByCategoryId(categoryId, page, size)).thenReturn(expectedResponse);

        mockMvc.perform(get(BASE_URL + "/categories/{id}", categoryId)
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Плов"))
                .andExpect(jsonPath("$.total_elements").value(1));

        Mockito.verify(foodService).getByCategoryId(categoryId, page, size);
    }

    @Test
    @DisplayName("get - Получение детальной информации о блюде")
    void get_ShouldReturnFoodDetailsResponse() throws Exception {
        Long foodId = 1L;
        var receiptItem1 = new ReceiptItemResponse("1L", BigDecimal.ONE);
        var receiptItem2 = new ReceiptItemResponse("2L", BigDecimal.ONE);
        var details = new FoodDetailsResponse(foodId, "Плов", "Плов",
                List.of(receiptItem1, receiptItem2), "AVAILABLE", BigDecimal.valueOf(10));

        when(foodService.get(foodId)).thenReturn(details);

        mockMvc.perform(get(BASE_URL + "/{id}", foodId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(foodId))
                .andExpect(jsonPath("$.name").value("Плов"))
                .andExpect(jsonPath("$.receipt", hasSize(2)))
                .andExpect(jsonPath("$.status").value("AVAILABLE"))
                .andExpect(jsonPath("$.price").value(BigDecimal.valueOf(10)));

        Mockito.verify(foodService).get(foodId);
    }
}