package uz.uzumtech.retail_service.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record OrderRequest(
        @NotNull
        @Positive
        Long cartId
) {}