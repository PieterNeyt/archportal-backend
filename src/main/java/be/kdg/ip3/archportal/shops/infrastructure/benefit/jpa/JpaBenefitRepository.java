package be.kdg.ip3.archportal.shops.infrastructure.benefit.jpa;

import be.kdg.ip3.archportal.shops.infrastructure.order.jpa.JpaOrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaBenefitRepository extends JpaRepository<JpaBenefitEntity, UUID> {
}
