package be.kdg.ip3.archportal.shops.infrastructure.benefit;

import be.kdg.ip3.archportal.shops.domain.benefit.Benefit;
import be.kdg.ip3.archportal.shops.domain.benefit.BenefitId;
import be.kdg.ip3.archportal.shops.domain.benefit.BenefitRepository;
import be.kdg.ip3.archportal.shops.domain.order.Order;
import be.kdg.ip3.archportal.shops.infrastructure.benefit.jpa.JpaBenefitEntity;
import be.kdg.ip3.archportal.shops.infrastructure.benefit.jpa.JpaBenefitRepository;
import be.kdg.ip3.archportal.shops.infrastructure.order.jpa.JpaOrderEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class DbBenefitRepository implements BenefitRepository {

    private final JpaBenefitRepository jpaRepo;

    public DbBenefitRepository(JpaBenefitRepository jpaRepo) {
        this.jpaRepo = jpaRepo;
    }

    @Override
    public void save(Benefit benefit) {
       jpaRepo.save(JpaBenefitEntity.fromDomain(benefit));
    }


    @Override
    public Optional<Benefit> findById(BenefitId benefitId) {
        return jpaRepo.findById(benefitId.id()).map(JpaBenefitEntity::toDomain);
    }

    @Override
    public List<Benefit> findAll() {
        return jpaRepo.findAll().stream()
                .map(JpaBenefitEntity::toDomain)
                .toList();
    }
}
