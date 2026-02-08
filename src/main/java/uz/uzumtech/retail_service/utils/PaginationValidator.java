package uz.uzumtech.retail_service.utils;

import lombok.experimental.UtilityClass;
import org.springframework.data.domain.PageRequest;

@UtilityClass
public class PaginationValidator {

    public static final int MAX_PAGE_SIZE = 100;

    public static PageRequest validate(int page, int size) {
        return PageRequest.of(page - 1, Math.min(size, MAX_PAGE_SIZE));
    }
}
