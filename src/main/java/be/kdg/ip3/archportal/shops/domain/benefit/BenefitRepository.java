package be.kdg.ip3.archportal.shops.domain.benefit;

import org.jmolecules.ddd.annotation.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BenefitRepository {
    void save(Benefit benefit);
    Optional<Benefit> findById(BenefitId benefitId);
    List<Benefit> findAll();
    List<Benefit> findAllByIdIn(List<BenefitId> ids);
}
