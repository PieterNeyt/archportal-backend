package be.kdg.ip3.archportal.gameService.application;

import be.kdg.ip3.archportal.gameService.api.dto.GameStudioDto;
import be.kdg.ip3.archportal.gameService.domain.gamestudio.GameStudio;
import be.kdg.ip3.archportal.gameService.domain.gamestudio.GameStudioRepository;
import be.kdg.ip3.archportal.gameService.domain.gamestudio.OwnerId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class GameStudioService {
    private final  GameStudioRepository repository;
    public GameStudioService(GameStudioRepository repository) {
        this.repository = repository;
    }

    public GameStudio createGameStudio(GameStudioDto studioDto) {
        //TODO: later owner ook aanmaken voor de studio


        var studio = new GameStudio(OwnerId.create(),
                studioDto.name(),
                studioDto.description(),
                studioDto.IBAN());
        this.repository.save(studio);

        return studio;
    }
}
