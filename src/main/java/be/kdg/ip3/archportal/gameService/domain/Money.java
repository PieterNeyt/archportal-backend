package be.kdg.ip3.archportal.gameService.domain;

import java.math.BigDecimal;

public record Money(BigDecimal money) {
    public static Money of(BigDecimal money) {
        return new Money(money);
    }
}
