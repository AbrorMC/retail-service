package uz.uzumtech.retail_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import uz.uzumtech.retail_service.dto.response.ReceiptItemResponse;
import uz.uzumtech.retail_service.entity.ReceiptItem;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ReceiptItemMapper extends BaseMapper<ReceiptItemResponse, ReceiptItem> {

    @Mapping(target = "ingredient", source = "ingredient.name")
    ReceiptItemResponse toResponse(ReceiptItem receiptItem);

}
