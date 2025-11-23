package be.kdg.ip3.archportal.shops.infrastructure.order.jpa;

import be.kdg.ip3.archportal.shops.domain.order.Order;
import be.kdg.ip3.archportal.shops.domain.order.OrderId;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Table(name = "orders", schema = "shopservice")
public class JpaOrderEntity {

    @Id
    private UUID id;

    @Column(nullable = false)
    private UUID profileId;

    @OneToMany(
            mappedBy = "order",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<JpaOrderLineEntity> orderLines = new ArrayList<>();

    protected JpaOrderEntity() {}

    public JpaOrderEntity(UUID id, UUID profileId, List<JpaOrderLineEntity> orderLines) {
        this.id = id;
        this.profileId = profileId;
        this.orderLines = orderLines;
    }

    public static JpaOrderEntity fromDomain(Order order) {
        JpaOrderEntity jpaOrder = new JpaOrderEntity(
                order.getOrderId().id(),
                order.getProfileId(),
                new ArrayList<>()
        );

        order.getOrderLines().forEach(line ->
                jpaOrder.orderLines.add(
                        JpaOrderLineEntity.fromDomain(line, jpaOrder)
                )
        );

        return jpaOrder;
    }

    public Order toDomain() {
        return new Order(
                new OrderId(id),
                profileId,
                orderLines.stream()
                        .map(JpaOrderLineEntity::toDomain)
                        .toList()
        );
    }
}