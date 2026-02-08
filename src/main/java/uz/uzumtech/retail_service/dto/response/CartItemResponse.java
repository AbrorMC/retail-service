package uz.uzumtech.retail_service.dto.response;

public record CartItemResponse(
        Long id,
        String food,
        Integer count
) {
}
