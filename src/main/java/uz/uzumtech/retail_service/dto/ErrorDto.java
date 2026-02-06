package uz.uzumtech.retail_service.dto;

import lombok.Builder;
import uz.uzumtech.retail_service.constant.enums.ErrorType;

import java.util.List;

@Builder
public record ErrorDto(
        int code,
        String message,
        ErrorType type,
        List<String> validationErrors
) {}
