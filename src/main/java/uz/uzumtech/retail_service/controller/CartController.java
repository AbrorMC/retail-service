package uz.uzumtech.retail_service.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.uzumtech.retail_service.dto.request.CartItemRequest;
import uz.uzumtech.retail_service.dto.response.CartItemResponse;
import uz.uzumtech.retail_service.service.CartService;

import java.util.List;

@RestController
@RequestMapping("api/core/cart")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CartController {

    CartService cartService;

    @PostMapping
    ResponseEntity<CartItemResponse> add(@RequestBody CartItemRequest request) {
        return ResponseEntity.ok(cartService.addItem(request));
    }

    @GetMapping("/{id}")
    ResponseEntity<List<CartItemResponse>> getAll(@PathVariable Long id) {
        return ResponseEntity.ok(cartService.getItemsOfCart(id));
    }
}
