package uz.uzumtech.retail_service.service;

import org.springframework.data.domain.Pageable;
import uz.uzumtech.retail_service.dto.response.FoodDetailsResponse;
import uz.uzumtech.retail_service.dto.response.FoodResponse;
import uz.uzumtech.retail_service.dto.response.PageResponse;

public interface FoodService {

    PageResponse<FoodResponse> getByCategoryId(Long id, Pageable request);
    FoodDetailsResponse get(Long id);

}
