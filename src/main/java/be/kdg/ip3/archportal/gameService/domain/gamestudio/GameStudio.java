package be.kdg.ip3.archportal.gameService.domain.gamestudio;

import lombok.Getter;

@Getter
public class GameStudio {
    private final GameStudioId id;
    private final OwnerId ownerId;
    private String name;
    private String description;
    private String IBAN;

    public GameStudio(OwnerId ownerId, String name, String description, String IBAN) {
        this.id = GameStudioId.create();
        this.ownerId = ownerId;
        this.name = name;
        this.description = description;
        this.IBAN = IBAN;
    }

    public GameStudio(GameStudioId id, OwnerId ownerId, String name, String description, String IBAN) {
        this.id = id;
        this.ownerId = ownerId;
        this.name = name;
        this.description = description;
        this.IBAN = IBAN;
    }
}
