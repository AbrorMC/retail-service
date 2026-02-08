package uz.uzumtech.retail_service.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.uzumtech.retail_service.dto.response.FoodDetailsResponse;
import uz.uzumtech.retail_service.dto.response.FoodResponse;
import uz.uzumtech.retail_service.dto.response.PageResponse;
import uz.uzumtech.retail_service.service.FoodService;

@RestController
@RequestMapping("api/core/foods")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class FoodController {

    FoodService foodService;

    @GetMapping("/categories/{id}")
    public ResponseEntity<PageResponse<FoodResponse>> getFoodsByCategoryId(@PathVariable Long id, @RequestParam("size") int size, @RequestParam("page") int page) {
        return ResponseEntity.ok(foodService.getByCategoryId(id, page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FoodDetailsResponse> get(@PathVariable Long id) {
        return ResponseEntity.ok(foodService.get(id));
    }

}
