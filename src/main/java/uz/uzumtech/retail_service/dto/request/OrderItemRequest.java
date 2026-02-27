package uz.uzumtech.retail_service.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record OrderItemRequest(

        @NotNull
        @Positive(message = "positive")
        Long cartId,

        @NotNull
        @Positive
        Long foodId,

        @NotNull
        @Positive
        Integer count
) {}