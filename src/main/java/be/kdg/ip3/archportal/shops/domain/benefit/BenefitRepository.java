package be.kdg.ip3.archportal.shops.domain.benefit;

import be.kdg.ip3.archportal.shops.domain.order.Order;
import org.jmolecules.ddd.annotation.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BenefitRepository {
    void save(Benefit benefit);
    Optional<Benefit> findById(BenefitId benefitId);
    List<Benefit> findAll();
    List<Benefit> findAllByIdIn(List<BenefitId> ids);
}
