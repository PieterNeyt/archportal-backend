package be.kdg.ip3.archportal.shops.domain.cart;

import org.jmolecules.ddd.annotation.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CartRepository {
    Optional<Cart> findByProfileId(UUID profileId);
    Cart save(Cart cart);
}
