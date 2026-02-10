package uz.uzumtech.retail_service.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.uzumtech.retail_service.dto.request.OrderItemRequest;
import uz.uzumtech.retail_service.dto.response.OrderItemResponse;
import uz.uzumtech.retail_service.dto.response.CartResponse;
import uz.uzumtech.retail_service.service.CartService;

@RestController
@RequestMapping("api/core/cart")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CartController {

    CartService cartService;

    @PostMapping
    ResponseEntity<OrderItemResponse> add(@RequestBody OrderItemRequest request) {
        return ResponseEntity.ok(cartService.addItem(request));
    }

    @GetMapping("/{id}")
    ResponseEntity<CartResponse> getAll(@PathVariable Long id) {
        return ResponseEntity.ok(cartService.getItemsOfCart(id));
    }
}
