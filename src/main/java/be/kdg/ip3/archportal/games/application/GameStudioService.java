package be.kdg.ip3.archportal.games.application;

import be.kdg.ip3.archportal.games.api.dto.OwnerStudioStatusDto;
import be.kdg.ip3.archportal.games.application.command.GameStudioCommand;
import be.kdg.ip3.archportal.games.domain.NotFoundException;
import be.kdg.ip3.archportal.games.domain.event.GameStudioCreatedEvent;
import be.kdg.ip3.archportal.games.domain.gamestudio.GameStudio;
import be.kdg.ip3.archportal.games.domain.gamestudio.GameStudioId;
import be.kdg.ip3.archportal.games.domain.gamestudio.GameStudioRepository;
import be.kdg.ip3.archportal.games.domain.owner.OwnerAlreadyHasGameStudioException;
import be.kdg.ip3.archportal.games.domain.owner.OwnerId;
import be.kdg.ip3.archportal.games.domain.owner.OwnerRepository;
import be.kdg.ip3.archportal.profiles.shared.ProfilesApi;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class GameStudioService {
    private final GameStudioRepository gameStudioRepo;
    private final OwnerRepository ownerRepo;
    private final ProfilesApi profilesApi;
    private final ApplicationEventPublisher publisher;

    public GameStudioService(GameStudioRepository repository, OwnerRepository ownerRepo, ProfilesApi profilesApi, ApplicationEventPublisher publisher) {
        this.gameStudioRepo = repository;
        this.ownerRepo = ownerRepo;
        this.profilesApi = profilesApi;
        this.publisher = publisher;
    }

    public GameStudioCommand createGameStudio(GameStudioCommand studioCommand) {
        if (!profilesApi.existsById(studioCommand.ownerId().id()))
            throw new NotFoundException("Profile [" + studioCommand.ownerId().id() + "] not found");

        if (ownerRepo.existsById(studioCommand.ownerId()))
            throw new OwnerAlreadyHasGameStudioException(studioCommand.ownerId());

        var studio = new GameStudio(
                studioCommand.ownerId(),
                studioCommand.name(),
                studioCommand.description(),
                studioCommand.IBAN());

        this.gameStudioRepo.save(studio);
        var gameStudioCreatedEvent = new GameStudioCreatedEvent(studio.getOwnerId(), studio.getId());
        publisher.publishEvent(gameStudioCreatedEvent);

        return GameStudioCommand.fromDomain(studio, studioCommand.ownerId());
    }

    public GameStudio findById(GameStudioId id) {
        return this.gameStudioRepo.findById(id).orElseThrow(id::notFound);
    }
    
    public GameStudio findByOwnerId(OwnerId ownerId) {
        return this.gameStudioRepo.findByOwnerId(ownerId).orElseThrow(ownerId::notFound);
    }

    public OwnerStudioStatusDto findGameStudioStatusByOwnerId(OwnerId ownerId) {
        return this.gameStudioRepo.findByOwnerId(ownerId).map(OwnerStudioStatusDto::from).orElseGet(OwnerStudioStatusDto::noStudio);
    }

    public GameStudio updateGameStudio(GameStudioCommand studioCommand) {
        var studio = this.gameStudioRepo.findById(studioCommand.id())
                .orElseThrow(studioCommand.id()::notFound);

        studio.update(studioCommand.toDomain(),studioCommand.ownerId());
        this.gameStudioRepo.save(studio);
        return  studio;
    }
}
