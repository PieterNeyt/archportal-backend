package be.kdg.ip3.archportal.shops.shared;

import be.kdg.ip3.archportal.shops.domain.benefit.Benefit;
import be.kdg.ip3.archportal.shops.domain.benefit.BenefitType;

import java.util.UUID;

public record GlobalBenefitDto(
        UUID id,
        BenefitType type,
        String name,
        String description,
        int pointCost,
        String configuration
) {
    public static GlobalBenefitDto fromDomain(Benefit benefit) {
        return new GlobalBenefitDto(
                benefit.getBenefitId().id(),
                benefit.getType(),
                benefit.getName(),
                benefit.getDescription(),
                benefit.getPointCost(),
                benefit.getConfiguration()
        );
    }
}