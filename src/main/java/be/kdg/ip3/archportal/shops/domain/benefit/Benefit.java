package be.kdg.ip3.archportal.shops.domain.benefit;

import lombok.Getter;
import org.jmolecules.ddd.annotation.AggregateRoot;
import org.jmolecules.ddd.annotation.Identity;

@Getter
@AggregateRoot
public class Benefit {

    @Identity
    private final BenefitId benefitId;

    private final BenefitType type;
    private final String name;
    private final String description;
    private final int pointCost;

    private final String configuration;

    public Benefit(BenefitType type, String name, String description, int pointCost, String configuration) {
      this(BenefitId.create(), type, name, description, pointCost, configuration);
    }

    public Benefit(BenefitId benefitId, BenefitType type, String name, String description, int pointCost, String configuration) {
        this.benefitId = benefitId;
        this.type = type;
        this.name = name;
        this.description = description;
        this.pointCost = pointCost;
        this.configuration = configuration;
    }

}