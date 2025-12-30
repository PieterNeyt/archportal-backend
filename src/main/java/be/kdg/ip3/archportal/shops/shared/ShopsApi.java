package be.kdg.ip3.archportal.shops.shared;

import org.springframework.modulith.NamedInterface;

import java.util.UUID;

@NamedInterface
public interface ShopsApi {
    GlobalBenefitDto getBenefitById(UUID benefitId);

}
