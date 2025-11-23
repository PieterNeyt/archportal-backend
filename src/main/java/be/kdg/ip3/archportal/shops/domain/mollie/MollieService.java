package be.kdg.ip3.archportal.shops.domain.mollie;

import be.kdg.ip3.archportal.shops.api.dto.PaymentCreationDto;
import be.woutschoovaerts.mollie.Client;
import be.woutschoovaerts.mollie.data.common.Amount;
import be.woutschoovaerts.mollie.data.payment.PaymentRequest;
import be.woutschoovaerts.mollie.data.payment.PaymentResponse;
import be.woutschoovaerts.mollie.data.payment.PaymentStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MollieService implements IMollieService {

    private final Client mollieClient;

    @Value("${mollie.redirect.url}")
    private String redirectUrl;

    public PaymentCreationDto createPayment(BigDecimal amount, String description, UUID orderId) {
        try {
            Amount paymentAmount = Amount.builder()
                    .currency("EUR")
                    .value(amount)
                    .build();

            String redirectUrlWithOrderId = redirectUrl + "?orderId=" + orderId;

            PaymentRequest request = PaymentRequest.builder()
                    .amount(paymentAmount)
                    .description(description)
                    .redirectUrl(redirectUrlWithOrderId)
                    .metadata(Map.of("orderId", orderId.toString()))
                    .build();

            PaymentResponse paymentResponse = mollieClient.payments().createPayment(request);

            return new PaymentCreationDto(
                    paymentResponse.getLinks().getCheckout().getHref(),
                    paymentResponse.getId()
            );

        } catch (Exception e) {
            throw new RuntimeException("Payment creation failed", e);
        }
    }


    public boolean verifyPayment(String paymentId) {
        try {
            PaymentResponse payment = mollieClient.payments().getPayment(paymentId);
            return payment.getStatus() == PaymentStatus.PAID;
        } catch (Exception e) {
            return false;
        }
    }

}
