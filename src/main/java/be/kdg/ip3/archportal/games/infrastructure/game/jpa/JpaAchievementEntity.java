package be.kdg.ip3.archportal.games.infrastructure.game.jpa;

import be.kdg.ip3.archportal.games.domain.achievement.Achievement;
import be.kdg.ip3.archportal.games.domain.achievement.AchievementId;
import be.kdg.ip3.archportal.games.domain.achievement.ExternalAchId;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.UUID;

@Entity
@Getter
@Table(name = "achievements", schema = "gameservice")
public class JpaAchievementEntity {
    @Id
    private UUID id;

    @Column(nullable = false)
    private UUID externalAchId;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id")
    private JpaGameEntity game;

    protected JpaAchievementEntity() {
    }

    public JpaAchievementEntity(UUID id, UUID externalAchId, String title, String description, String imageUrl, JpaGameEntity game) {
        this.id = id;
        this.externalAchId = externalAchId;
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.game = game;
    }

    public static JpaAchievementEntity fromDomain(Achievement achievement, JpaGameEntity gameEntity) {
        return new JpaAchievementEntity(
                achievement.getId().id(),
                achievement.getExternalAchId().id(),
                achievement.getTitle(),
                achievement.getDescription(),
                achievement.getImageUrl(),
                gameEntity
        );
    }

    public Achievement toDomain() {
        return new Achievement(
                new AchievementId(id),
                title,
                description,
                imageUrl,
                new ExternalAchId(id)
        );
    }
}
