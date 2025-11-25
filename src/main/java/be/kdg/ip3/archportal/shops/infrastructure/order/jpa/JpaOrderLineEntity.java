package be.kdg.ip3.archportal.shops.infrastructure.order.jpa;

import be.kdg.ip3.archportal.shops.domain.order.OrderLine;
import be.kdg.ip3.archportal.shops.domain.order.OrderLineId;
import jakarta.persistence.*;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Getter
@Table(name = "order_line", schema = "shopservice")
public class JpaOrderLineEntity {

    @Id
    private UUID id;

    @Column(nullable = false)
    private UUID gameId;

    @Column(nullable = false)
    private BigDecimal price;

    @ManyToOne()
    @JoinColumn(name = "order_id", nullable = false)
    private JpaOrderEntity order;

    protected JpaOrderLineEntity() {}

    public JpaOrderLineEntity(UUID id, UUID gameId, BigDecimal price, JpaOrderEntity order) {
        this.id = id;
        this.gameId = gameId;
        this.price = price;
        this.order = order;
    }

    public static JpaOrderLineEntity fromDomain(OrderLine line, JpaOrderEntity order) {
        return new JpaOrderLineEntity(
                line.getOrderLineId().id(),
                line.getGameId(),
                line.getPrice(),
                order
        );
    }

    public OrderLine toDomain() {
        return new OrderLine(
                new OrderLineId(id),
                gameId,
                price
        );
    }
}