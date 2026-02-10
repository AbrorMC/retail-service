package uz.uzumtech.retail_service.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record OrderRequest(

        @JsonProperty("cart_id")
        Long cartId
) {}
