package be.kdg.ip3.archportal.profiles.shared;

import org.springframework.modulith.NamedInterface;

import java.util.UUID;

@NamedInterface
public interface ProfilesApi {
    boolean existsById(UUID id);
}
