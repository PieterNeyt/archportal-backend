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

    public Order(UUID profileId) {
        orderId = OrderId.create();
        this.profileId = profileId;
        this.orderLines = new ArrayList<>();
    }

    public Order(OrderId orderId, UUID profileId, List<OrderLine> orderLines, String paymentId) {
        this.orderId = orderId;
        this.profileId = profileId;
        this.orderLines = orderLines;
        this.paymentId = paymentId;
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
    public void createPayment(String paymentId) {
        this.paymentId = paymentId;
    }
}