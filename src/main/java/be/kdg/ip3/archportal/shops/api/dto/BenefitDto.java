package be.kdg.ip3.archportal.shops.api.dto;


import be.kdg.ip3.archportal.shops.domain.benefit.Benefit;
import be.kdg.ip3.archportal.shops.domain.benefit.BenefitType;

import java.util.UUID;

public record BenefitDto(
        UUID id,
        BenefitType type,
        String name,
        String description,
        int pointCost,
        String configuration
) {
    public static BenefitDto fromDomain(Benefit benefit) {
        return new BenefitDto(
                benefit.getBenefitId().id(),
                benefit.getType(),
                benefit.getName(),
                benefit.getDescription(),
                benefit.getPointCost(),
                benefit.getConfiguration()
        );
    }
}