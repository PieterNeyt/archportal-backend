package be.kdg.ip3.archportal.games.application;

import be.kdg.ip3.archportal.games.application.command.CreateGameStudioCommand;
import be.kdg.ip3.archportal.games.domain.gamestudio.GameStudio;
import be.kdg.ip3.archportal.games.domain.gamestudio.GameStudioId;
import be.kdg.ip3.archportal.games.domain.gamestudio.GameStudioRepository;
import be.kdg.ip3.archportal.games.domain.owner.Owner;
import be.kdg.ip3.archportal.games.domain.owner.OwnerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class GameStudioService {
    private final  GameStudioRepository gameStudioRepo;
    private final OwnerRepository ownerRepo;

    public GameStudioService(GameStudioRepository repository, OwnerRepository ownerRepo) {
        this.gameStudioRepo = repository;
        this.ownerRepo = ownerRepo;
    }

    // TODO dit omvormen (accounts worden aangemaakt via keycloak)
    public CreateGameStudioCommand createGameStudio(CreateGameStudioCommand studioCommand) {
        var owner = new Owner(
                studioCommand.ownerFirstName(),
                studioCommand.ownerLastName(),
                studioCommand.ownerEmail());

        this.ownerRepo.save(owner);

        var studio = new GameStudio(
                owner.getId(),
                studioCommand.name(),
                studioCommand.description(),
                studioCommand.IBAN());

        this.gameStudioRepo.save(studio);

        return CreateGameStudioCommand.fromDomain(studio,owner);
    }
    
    public GameStudio findById(GameStudioId id) {
        return this.gameStudioRepo.findById(id).orElseThrow(id::notFound);
    }
}
