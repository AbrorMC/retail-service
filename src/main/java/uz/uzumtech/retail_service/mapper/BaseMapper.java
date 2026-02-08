package uz.uzumtech.retail_service.mapper;

import org.springframework.data.domain.Page;
import uz.uzumtech.retail_service.dto.response.PageResponse;

public interface BaseMapper<T, P> {

    T toResponse(P entity);

    default PageResponse<T> toPageResponse(Page<P> page) {
        return new PageResponse<>(
                page.getContent().stream().map(this::toResponse).toList(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.getNumber() + 1,
                page.isLast()
        );
    }
}
