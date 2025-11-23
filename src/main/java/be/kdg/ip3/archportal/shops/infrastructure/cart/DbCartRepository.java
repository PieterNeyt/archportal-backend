package be.kdg.ip3.archportal.shops.infrastructure.cart;

import be.kdg.ip3.archportal.shops.domain.cart.Cart;
import be.kdg.ip3.archportal.shops.domain.cart.CartRepository;
import be.kdg.ip3.archportal.shops.infrastructure.cart.jpa.JpaCartEntity;
import be.kdg.ip3.archportal.shops.infrastructure.cart.jpa.JpaCartRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class DbCartRepository implements CartRepository {
    private final JpaCartRepository jpaRepo;

    public DbCartRepository(JpaCartRepository jpaRepo) {
        this.jpaRepo = jpaRepo;
    }

    @Override
    public Optional<Cart> findByProfileId(UUID profileId) {
        return jpaRepo.findByProfileId(profileId)
                .map(JpaCartEntity::toDomain);
    }

    @Override
    public Cart save(Cart cart) {
        JpaCartEntity saved = jpaRepo.save(JpaCartEntity.fromDomain(cart));
        return saved.toDomain();
    }

    @Override
    public void delete(Cart cart) {
        jpaRepo.delete(JpaCartEntity.fromDomain(cart));
    }
}
