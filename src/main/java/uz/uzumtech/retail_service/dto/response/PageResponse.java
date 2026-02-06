package uz.uzumtech.retail_service.dto.response;

import java.util.List;

public record PageResponse<T> (
        List<T> content,
        long totalElements,
        int totalPages,
        int currentPage,
        boolean isLast
) {
}
