package be.kdg.ip3.archportal.shops.domain.order;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import be.kdg.ip3.archportal.games.shared.GlobalGameDto;
import be.kdg.ip3.archportal.shops.domain.benefit.Benefit;
import be.kdg.ip3.archportal.shops.domain.benefit.BenefitType;
import be.kdg.ip3.archportal.shops.domain.cart.Cart;
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
    private BigDecimal appliedBenefitPercentage;
    private final List<OrderLine> orderLines;
    private boolean completed;

    public void markAsCompleted() {
        this.completed = true;
    }

    public Order(UUID profileId) {
        orderId = OrderId.create();
        this.profileId = profileId;
        this.orderLines = new ArrayList<>();
        this.completed = false;
        this.appliedBenefitPercentage = BigDecimal.ZERO;
    }

    public Order(OrderId orderId, UUID profileId, List<OrderLine> orderLines, String paymentId, boolean completed, BigDecimal appliedBenefitPercentage) {
        this.orderId = orderId;
        this.profileId = profileId;
        this.orderLines = orderLines;
        this.paymentId = paymentId;
        this.completed = completed;
        this.appliedBenefitPercentage = appliedBenefitPercentage;
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

    public BigDecimal applyDiscount(Benefit benefit) {
        if (benefit.getType() != BenefitType.GAME_DISCOUNT) {
            return totalPrice();
        }

        var total = totalPrice();
        var discountPercent = new BigDecimal(benefit.getConfiguration().replace("%", ""))
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        var discountAmount = total.multiply(discountPercent);

        this.appliedBenefitPercentage = discountPercent;

        return total.subtract(discountAmount);
    }

    public static Order createFromCart(UUID profileId, List<GlobalGameDto> games) {
        var order = new Order(profileId);
        games.forEach(game -> order.addOrderLine(game.id(), game.price()));

        return order;
    }

}