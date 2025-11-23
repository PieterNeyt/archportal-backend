
package be.kdg.ip3.archportal.shops.domain.cart;

import org.springframework.util.Assert;

import java.util.UUID;

public record CartId(UUID id) {

    public CartId {
        Assert.notNull(id, "id is null");
    }

    public static CartId create() {
        return new CartId(UUID.randomUUID());
    }
}
