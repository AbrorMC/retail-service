package uz.uzumtech.retail_service.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.uzumtech.retail_service.dto.response.FoodResponse;
import uz.uzumtech.retail_service.mapper.FoodMapper;
import uz.uzumtech.retail_service.repository.FoodRepository;
import uz.uzumtech.retail_service.service.FoodService;
import uz.uzumtech.retail_service.utils.PaginationValidator;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class FoodServiceImpl implements FoodService {

    FoodMapper foodMapper;
    FoodRepository foodRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<FoodResponse> getByCategoryId(Long id, int pageNumber, int size) {
        var pageable = PaginationValidator.validate(pageNumber, size);

        return foodRepository
                .findAllByCategoryId(id, pageable)
                .map(foodMapper::toResponse);
    }

}
