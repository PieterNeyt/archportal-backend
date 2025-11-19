package be.kdg.ip3.archportal.gameService.application;

import be.kdg.ip3.archportal.gameService.api.dto.CreateGameStudioDto;
import be.kdg.ip3.archportal.gameService.domain.gamestudio.GameStudio;
import be.kdg.ip3.archportal.gameService.domain.gamestudio.GameStudioRepository;
import be.kdg.ip3.archportal.gameService.domain.owner.Owner;
import be.kdg.ip3.archportal.gameService.domain.owner.OwnerRepository;
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

    public GameStudio createGameStudio(CreateGameStudioDto studioDto) {
        var owner = new Owner(
                studioDto.ownerFirstName(),
                studioDto.ownerLastName(),
                studioDto.ownerEmail());

        this.ownerRepo.save(owner);

        var studio = new GameStudio(
                owner.getId(),
                studioDto.name(),
                studioDto.description(),
                studioDto.IBAN());

        this.gameStudioRepo.save(studio);
        return studio;
    }
}
