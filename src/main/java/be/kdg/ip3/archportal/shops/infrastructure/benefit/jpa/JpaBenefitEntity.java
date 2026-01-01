package be.kdg.ip3.archportal.shops.infrastructure.benefit.jpa;

import be.kdg.ip3.archportal.shops.domain.benefit.Benefit;
import be.kdg.ip3.archportal.shops.domain.benefit.BenefitId;
import be.kdg.ip3.archportal.shops.domain.benefit.BenefitType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "benefits", schema = "shopservice")
public class JpaBenefitEntity {

    @Id
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BenefitType type;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false)
    private int pointCost;

    private String configuration;

    public JpaBenefitEntity(UUID id, BenefitType type, String name, String description, int pointCost, String configuration) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.description = description;
        this.pointCost = pointCost;
        this.configuration = configuration;
    }

    public static JpaBenefitEntity fromDomain(Benefit benefit) {
        return new JpaBenefitEntity(
                benefit.getBenefitId().id(),
                benefit.getType(),
                benefit.getName(),
                benefit.getDescription(),
                benefit.getPointCost(),
                benefit.getConfiguration()
        );
    }

    public Benefit toDomain() {
        return new Benefit(
                new BenefitId(id),
                type,
                name,
                description,
                pointCost,
                configuration
        );
    }
}