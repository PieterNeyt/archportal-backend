package be.kdg.ip3.archportal.shops.domain.cart;

import lombok.Getter;
import org.jmolecules.ddd.annotation.AggregateRoot;
import org.jmolecules.ddd.annotation.Identity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
@Getter
@AggregateRoot
public class Cart {

    @Identity
    private final CartId cartId;

    private final UUID profileId;
    private final List<UUID> cartItems;

    public Cart(UUID profileId) {
        this(CartId.create(), profileId, new ArrayList<>());
    }

    public Cart(CartId cartId, UUID profileId, List<UUID> cartItems) {
        this.cartId = cartId;
        this.profileId = profileId;
        this.cartItems = cartItems;
    }

    public void addToCart(UUID gameId) {
        this.cartItems.add(gameId);
    }

    public void removeFromCart(UUID gameId) {
        this.cartItems.remove(gameId);
    }

    public void validateForCheckout() {
        if (cartItems.isEmpty()) {
            throw new IllegalStateException("Cannot checkout with an empty cart");
        }
    }
}
