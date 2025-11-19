package be.kdg.ip3.archportal.gameService.domain.gamestudio;

import org.jmolecules.ddd.annotation.Repository;

@Repository
public interface GameStudioRepository {
    void save(GameStudio studio);
}
