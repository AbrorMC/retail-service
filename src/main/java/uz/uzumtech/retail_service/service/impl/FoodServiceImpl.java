package uz.uzumtech.retail_service.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.uzumtech.retail_service.constant.enums.FoodAvailability;
import uz.uzumtech.retail_service.dto.response.FoodDetailsResponse;
import uz.uzumtech.retail_service.dto.response.FoodResponse;
import uz.uzumtech.retail_service.dto.response.PageResponse;
import uz.uzumtech.retail_service.entity.Food;
import uz.uzumtech.retail_service.entity.Price;
import uz.uzumtech.retail_service.exception.FoodNotFoundException;
import uz.uzumtech.retail_service.exception.PriceNotFoundException;
import uz.uzumtech.retail_service.mapper.FoodMapper;
import uz.uzumtech.retail_service.repository.FoodRepository;
import uz.uzumtech.retail_service.repository.PriceRepository;
import uz.uzumtech.retail_service.service.FoodService;
import uz.uzumtech.retail_service.utils.PaginationValidator;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class FoodServiceImpl implements FoodService {

    FoodMapper foodMapper;
    FoodRepository foodRepository;
    PriceRepository priceRepository;

    @Override
    @Transactional(readOnly = true)
    public PageResponse<FoodResponse> getByCategoryId(Long id, int pageNumber, int size) {
        var pageable = PaginationValidator.validate(pageNumber, size);

        var page = foodRepository.findAllByCategoryId(id, pageable);

        return foodMapper.toPageResponse(page);
    }

    @Override
    @Transactional(readOnly = true)
    public FoodDetailsResponse get(Long id) {
        var food = foodRepository
                .findById(id)
                .orElseThrow(() -> new FoodNotFoundException(id.toString()));

        var price = priceRepository
                .findByFoodIdAndIsActiveTrue(id)
                .orElseThrow(() -> new PriceNotFoundException(id.toString()));

        int availableServings = foodRepository.getAvailableServings(id);

        FoodAvailability availability = switch (availableServings) {
            case 0 -> FoodAvailability.NOT_AVAILABLE;
            case 1 -> FoodAvailability.ONE;
            case 2 -> FoodAvailability.TWO;
            case 3, 4, 5 -> FoodAvailability.ENDING;
            default -> FoodAvailability.AVAILABLE;
        };

        return foodMapper.toDetailedResponse(food, availability, price.getPrice());
    }

}
