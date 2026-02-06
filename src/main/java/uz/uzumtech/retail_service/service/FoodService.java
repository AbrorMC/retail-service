package uz.uzumtech.retail_service.service;

import org.springframework.data.domain.Page;
import uz.uzumtech.retail_service.dto.response.FoodResponse;

public interface FoodService {

    Page<FoodResponse> getByCategoryId(Long id, int pageNumber, int size);
    //FoodDetailsResponse get(Long id);

}
