package be.kdg.ip3.archportal.shops.domain.order;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import lombok.Getter;
import org.jmolecules.ddd.annotation.AggregateRoot;
import org.jmolecules.ddd.annotation.Identity;

@Getter
@AggregateRoot
public class Order {

    @Identity
    private final OrderId orderId;

    private final UUID profileId;
    private String paymentId;
    private final List<OrderLine> orderLines;
    private boolean completed;

    private UUID appliedBenefitId;

    public void markAsCompleted() {
        this.completed = true;
    }

    public Order(UUID profileId) {
        orderId = OrderId.create();
        this.profileId = profileId;
        this.orderLines = new ArrayList<>();
        this.completed = false;
    }

    public Order(OrderId orderId, UUID profileId, List<OrderLine> orderLines, String paymentId, boolean completed, UUID appliedBenefitId) {
        this.orderId = orderId;
        this.profileId = profileId;
        this.orderLines = orderLines;
        this.paymentId = paymentId;
        this.completed = completed;
        this.appliedBenefitId = appliedBenefitId;
    }

    public void addOrderLine(UUID gameId, BigDecimal price) {
        orderLines.add(new OrderLine(gameId, price));
    }

    public BigDecimal totalPrice() {
        return orderLines
                .stream()
                .map(OrderLine::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void attachPayment(String paymentId) {
        this.paymentId = paymentId;
    }

    public void addAppliedBenefitId(UUID appliedBenefitId) {
        this.appliedBenefitId = appliedBenefitId;
    }
}