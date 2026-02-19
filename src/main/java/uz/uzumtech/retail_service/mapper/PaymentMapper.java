package uz.uzumtech.retail_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import uz.uzumtech.retail_service.constant.enums.PaymentStatus;
import uz.uzumtech.retail_service.dto.request.PaymentRequest;
import uz.uzumtech.retail_service.entity.Payment;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PaymentMapper {

    PaymentRequest toRequest(Payment payment);

    @Mapping(target = "status", constant = "CREATED")
    Payment toEntity(PaymentRequest request);
}
