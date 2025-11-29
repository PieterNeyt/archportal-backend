package be.kdg.ip3.archportal.shops.domain;

public class NotFoundException extends RuntimeException {
    public NotFoundException(final String message) {
        super(message);
    }
}