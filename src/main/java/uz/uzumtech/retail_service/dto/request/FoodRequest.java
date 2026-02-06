package uz.uzumtech.retail_service.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.List;

public record FoodRequest(
        @NotBlank
        String name,

        @NotNull
        @Positive
        BigDecimal price,

        @NotNull
        Long categoryId,

        @NotNull
        @NotEmpty
        @Valid
        List<ReceiptItemRequest> receipt
) {
    public record ReceiptItemRequest(
            @NotNull
            Long ingredientId,

            @NotNull
            @Positive
            BigDecimal quantity
    ) {
    }
}
