package uz.uzumtech.retail_service.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.uzumtech.retail_service.dto.response.FoodDetailsResponse;
import uz.uzumtech.retail_service.dto.response.FoodResponse;
import uz.uzumtech.retail_service.dto.response.PageResponse;
import uz.uzumtech.retail_service.entity.Food;
import uz.uzumtech.retail_service.exception.FoodNotFoundException;
import uz.uzumtech.retail_service.mapper.FoodMapper;
import uz.uzumtech.retail_service.repository.FoodRepository;
import uz.uzumtech.retail_service.service.FoodService;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class FoodServiceImpl implements FoodService {

    FoodMapper foodMapper;
    FoodRepository foodRepository;

    @Override
    @Transactional(readOnly = true)
    public PageResponse<FoodResponse> getByCategoryId(Long id, Pageable request) {
        Page<Food> page = foodRepository.findAllByCategoryId(id, request);

        return foodMapper.toPageResponse(page);
    }

    @Override
    @Transactional(readOnly = true)
    public FoodDetailsResponse get(Long id) {
        var food = foodRepository
                .findById(id)
                .orElseThrow(() -> new FoodNotFoundException(id.toString()));


        return foodMapper.toDetailsResponse(food, BigDecimal.ONE);
    }

}
