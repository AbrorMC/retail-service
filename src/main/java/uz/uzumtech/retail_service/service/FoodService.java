package uz.uzumtech.retail_service.service;

import uz.uzumtech.retail_service.dto.response.FoodDetailsResponse;
import uz.uzumtech.retail_service.dto.response.FoodResponse;
import uz.uzumtech.retail_service.dto.response.PageResponse;

public interface FoodService {

    PageResponse<FoodResponse> getByCategoryId(Long id, int pageNumber, int size);
    FoodDetailsResponse get(Long id);

}
