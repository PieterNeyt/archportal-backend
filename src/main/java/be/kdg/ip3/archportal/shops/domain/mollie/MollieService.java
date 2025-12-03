package be.kdg.ip3.archportal.shops.domain.mollie;

import be.kdg.ip3.archportal.shops.api.dto.PaymentCreationDto;
import be.kdg.ip3.archportal.shops.domain.PaymentException;
import com.mollie.mollie.Client;
import com.mollie.mollie.models.components.Amount;
import com.mollie.mollie.models.components.PaymentRequest;
import com.mollie.mollie.models.components.PaymentResponseStatus;
import com.mollie.mollie.models.operations.CreatePaymentResponse;
import com.mollie.mollie.models.operations.GetPaymentRequest;
import com.mollie.mollie.models.operations.GetPaymentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MollieService implements IMollieService {

    private final Client mollieClient;

    @Value("${mollie.redirect.url}")
    private String redirectUrl;

    public PaymentCreationDto createPayment(BigDecimal amount, String description, UUID orderId) {
        try {
            String redirectUrlWithOrderId = redirectUrl + "?orderId=" + orderId;

            PaymentRequest request = PaymentRequest.builder()
                    .amount(Amount.builder()
                            .currency("EUR")
                            .value(amount.toString())
                            .build())
                    .description(description)
                    .redirectUrl(redirectUrlWithOrderId)
                    .build();

            CreatePaymentResponse response = mollieClient.payments().create()
                    .paymentRequest(request)
                    .call();

            if (response.paymentResponse().isPresent()) {
                var payment = response.paymentResponse().get();
                String paymentUrl = payment.links().checkout().orElseThrow().href();
                return new PaymentCreationDto(
                        paymentUrl,
                        payment.id()
                );
            }

            throw new PaymentException("Payment creation failed: no response");

        } catch (Exception e) {
            throw new PaymentException("Payment creation failed", e);
        }
    }

    public boolean verifyPayment(String paymentId) {
        try {
            GetPaymentRequest request = GetPaymentRequest.builder()
                    .paymentId(paymentId)
                    .build();

            GetPaymentResponse response = mollieClient.payments().get()
                    .request(request)
                    .call();

            if (response.paymentResponse().isPresent()) {
                var payment = response.paymentResponse().get();
                return payment.status() == PaymentResponseStatus.PAID;
            }

            return false;

        } catch (Exception e) {
            return false;
        }
    }
}
