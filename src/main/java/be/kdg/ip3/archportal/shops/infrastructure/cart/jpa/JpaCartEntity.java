package be.kdg.ip3.archportal.shops.infrastructure.cart.jpa;


import be.kdg.ip3.archportal.shops.domain.cart.Cart;
import be.kdg.ip3.archportal.shops.domain.cart.CartId;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Table(name = "cart", schema = "shopservice")
public class JpaCartEntity {

    @Id
    private UUID id;

    @Column(nullable = false)
    private UUID profileId;

    @ElementCollection
    @CollectionTable(
            name = "cart_items",
            schema = "shopservice",
            joinColumns = @JoinColumn(name = "cart_id")
    )
    @Column(name = "game_id", nullable = false)
    private List<UUID> cartItems = new ArrayList<>();

    protected JpaCartEntity() {}

    public JpaCartEntity(UUID id, UUID profileId, List<UUID> cartItems) {
        this.id = id;
        this.profileId = profileId;
        this.cartItems = cartItems;
    }

    public static JpaCartEntity fromDomain(Cart cart) {
        return new JpaCartEntity(
                cart.getCartId().id(),
                cart.getProfileId(),
                new ArrayList<>(cart.getCartItems())
        );
    }

    public Cart toDomain() {
        return new Cart(
                new CartId(id),
                profileId,
                new ArrayList<>(cartItems)
        );
    }
}