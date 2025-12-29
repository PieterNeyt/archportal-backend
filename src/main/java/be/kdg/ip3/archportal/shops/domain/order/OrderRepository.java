package be.kdg.ip3.archportal.shops.domain.order;

import be.kdg.ip3.archportal.shops.domain.cart.Cart;
import org.jmolecules.ddd.annotation.Repository;

import java.util.UUID;

@Repository
public interface OrderRepository {
    Order save(Order order);
    Order findById(UUID orderId);

}
