package be.kdg.ip3.archportal.games.application;

import be.kdg.ip3.archportal.games.domain.event.GameStudioCreatedEvent;
import be.kdg.ip3.archportal.games.domain.owner.Owner;
import be.kdg.ip3.archportal.games.domain.owner.OwnerRepository;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OwnerService {
    private final OwnerRepository ownerRepository;

    public OwnerService(OwnerRepository ownerRepository) {
        this.ownerRepository = ownerRepository;
    }

    @ApplicationModuleListener
    public void onGameStudioCreated(GameStudioCreatedEvent event) {
        if (ownerRepository.existsById(event.ownerId())) return;

        var owner = new Owner(event.ownerId(), event.gameStudioId());
        ownerRepository.save(owner);
    }
}
