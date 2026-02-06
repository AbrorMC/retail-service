package uz.uzumtech.retail_service.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CategoryRequest(

        @NotBlank
        String name,

        @NotBlank
        String description
) {
}
