package be.kdg.ip3.archportal.shops.infrastructure.order;

import be.kdg.ip3.archportal.shops.domain.order.Order;
import be.kdg.ip3.archportal.shops.domain.order.OrderRepository;
import be.kdg.ip3.archportal.shops.infrastructure.order.jpa.JpaOrderEntity;
import be.kdg.ip3.archportal.shops.infrastructure.order.jpa.JpaOrderRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class DbOrderRepository implements OrderRepository {

    private final JpaOrderRepository jpaRepo;

    public DbOrderRepository(JpaOrderRepository jpaRepo) {
        this.jpaRepo = jpaRepo;
    }

    @Override
    public Order save(Order order) {
        JpaOrderEntity saved = jpaRepo.save(JpaOrderEntity.fromDomain(order));
        return saved.toDomain();
    }

    @Override
    public Order findById(UUID orderId) {
        return jpaRepo.findById(orderId)
                .map(JpaOrderEntity::toDomain)
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + orderId));
    }

}
