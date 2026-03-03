package uz.uzumtech.retail_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import uz.uzumtech.retail_service.dto.request.OrderItemRequest;
import uz.uzumtech.retail_service.dto.response.CartResponse;
import uz.uzumtech.retail_service.dto.response.OrderItemResponse;
import uz.uzumtech.retail_service.service.CartService;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CartController.class)
public class CartControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CartService cartService;

    private final String BASE_URL = "/api/v1/retail/cart";

    @Test
    @DisplayName("add - Успешное добавление товара в корзину")
    void add_ShouldReturnOrderItemResponse() throws Exception {
        var request = new OrderItemRequest(1L, 1L, 2); // Предположим такие поля
        var response = new OrderItemResponse(1L, "1L", 2, BigDecimal.ZERO, BigDecimal.ZERO);

        when(cartService.addItem(any(OrderItemRequest.class))).thenReturn(response);

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.food").value("1L"))
                .andExpect(jsonPath("$.count").value(2));

        Mockito.verify(cartService).addItem(any(OrderItemRequest.class));
    }

    @Test
    @DisplayName("getAll - Получение содержимого корзины по ID, корзина не пустая")
    void getAll_ShouldReturnCartResponse() throws Exception {
        Long cartId = 1L;
        var response1 = new OrderItemResponse(1L, "1L", 2, BigDecimal.valueOf(50), BigDecimal.valueOf(100));
        var response2 = new OrderItemResponse(1L, "2L", 2, BigDecimal.valueOf(10), BigDecimal.valueOf(20));
        var response3 = new OrderItemResponse(1L, "3L", 2, BigDecimal.valueOf(30), BigDecimal.valueOf(60));

        var cartResponse = new CartResponse(3, BigDecimal.valueOf(180), List.of(response1, response2, response3));

        when(cartService.getItemsOfCart(cartId)).thenReturn(cartResponse);

        mockMvc.perform(get(BASE_URL + "/{id}", cartId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items", hasSize(3)))
                .andExpect(jsonPath("$.total_amount").value(BigDecimal.valueOf(180)))
                .andExpect(jsonPath("$.item_count").value(3));

        Mockito.verify(cartService).getItemsOfCart(cartId);
    }

    @Test
    @DisplayName("getAll - Получение содержимого корзины по ID, корзина пустая")
    void getAll_ShouldReturnEmptyCartResponse() throws Exception {
        Long cartId = 1L;
        var cartResponse = new CartResponse(0, BigDecimal.ZERO, List.of());

        when(cartService.getItemsOfCart(cartId)).thenReturn(cartResponse);

        mockMvc.perform(get(BASE_URL + "/{id}", cartId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items", hasSize(0)))
                .andExpect(jsonPath("$.total_amount").value(BigDecimal.ZERO))
                .andExpect(jsonPath("$.item_count").value(0));

        Mockito.verify(cartService).getItemsOfCart(cartId);
    }

    @Test
    @DisplayName("deleteItem - Успешное удаление товара из корзины")
    void deleteItem_ShouldReturnStatusOk() throws Exception {
        Long cartId = 1L;
        Long itemId = 1L;

        doNothing().when(cartService).deleteItem(cartId, itemId);

        mockMvc.perform(delete(BASE_URL + "/{cartId}/item/{itemId}", cartId, itemId))
                .andExpect(status().isOk());

        Mockito.verify(cartService, Mockito.times(1)).deleteItem(cartId, itemId);
    }

    @Test
    @DisplayName("clearCart - Полная очистка корзины")
    void clearCart_ShouldReturnStatusOk() throws Exception {
        Long cartId = 1L;

        doNothing().when(cartService).clear(cartId);

        mockMvc.perform(delete(BASE_URL + "/{cartId}", cartId))
                .andExpect(status().isOk());

        Mockito.verify(cartService).clear(cartId);
    }
}
