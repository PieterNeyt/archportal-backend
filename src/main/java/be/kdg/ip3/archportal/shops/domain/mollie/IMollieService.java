package be.kdg.ip3.archportal.shops.domain.mollie;


import be.kdg.ip3.archportal.shops.api.dto.PaymentCreationDto;

import java.math.BigDecimal;
import java.util.UUID;

public interface IMollieService {
    PaymentCreationDto createPayment(BigDecimal amount, String description, UUID orderId);
    boolean verifyPayment(String paymentId);
}
