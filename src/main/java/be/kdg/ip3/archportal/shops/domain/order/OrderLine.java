package be.kdg.ip3.archportal.shops.domain.order;

import lombok.Getter;
import org.jmolecules.ddd.annotation.Entity;
import org.jmolecules.ddd.annotation.Identity;

import java.math.BigDecimal;
import java.util.UUID;


@Getter
@Entity
public class OrderLine {
    @Identity
    private final OrderLineId orderLineId;
    private final UUID gameId;
    private final BigDecimal price;

    public OrderLine(UUID gameId, BigDecimal price) {
        this(OrderLineId.create(), gameId, price);
    }

    public OrderLine(OrderLineId orderLineId, UUID gameId, BigDecimal price) {
        this.orderLineId = orderLineId;
        this.gameId = gameId;
        this.price = price;
    }

}