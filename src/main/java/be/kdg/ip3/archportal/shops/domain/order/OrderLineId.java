package be.kdg.ip3.archportal.shops.domain.order;

import org.springframework.util.Assert;

import java.util.UUID;

public record OrderLineId(UUID id) {

    public OrderLineId {
        Assert.notNull(id, "id is null");
    }

    public static OrderLineId create() {
        return new OrderLineId(UUID.randomUUID());
    }
}
