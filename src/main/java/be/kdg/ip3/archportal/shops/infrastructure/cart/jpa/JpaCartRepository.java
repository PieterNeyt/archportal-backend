package be.kdg.ip3.archportal.shops.infrastructure.cart.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaCartRepository extends JpaRepository<JpaCartEntity, UUID> {
}
