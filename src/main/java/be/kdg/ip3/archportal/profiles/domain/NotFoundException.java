package be.kdg.ip3.archportal.profiles.domain;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
