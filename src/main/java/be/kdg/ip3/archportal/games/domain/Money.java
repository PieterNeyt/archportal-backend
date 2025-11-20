package be.kdg.ip3.archportal.games.domain;

import org.jmolecules.ddd.annotation.ValueObject;

import java.math.BigDecimal;

@ValueObject
public record Money(BigDecimal money) {
    public static Money of(BigDecimal money) {
        return new Money(money);
    }
}
