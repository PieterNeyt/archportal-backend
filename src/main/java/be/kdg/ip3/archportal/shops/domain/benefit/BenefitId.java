package be.kdg.ip3.archportal.shops.domain.benefit;

import be.kdg.ip3.archportal.profiles.domain.NotFoundException;
import org.springframework.util.Assert;

import java.util.UUID;

public record BenefitId(UUID id) {
    public BenefitId {
        Assert.notNull(id, "id is null");
    }

    public static BenefitId create() {
        return new BenefitId(UUID.randomUUID());
    }

    public static BenefitId create(UUID benefitId) {
        return new BenefitId(benefitId);
    }

    public NotFoundException notFound() {
        return new NotFoundException("Benefit [" + id + "] not found");
    }
}
