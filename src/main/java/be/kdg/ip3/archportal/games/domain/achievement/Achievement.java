package be.kdg.ip3.archportal.games.domain.achievement;

import lombok.Getter;
import org.jmolecules.ddd.annotation.Entity;

import java.util.UUID;

@Entity
@Getter
public class Achievement {
    private final AchievementId id;
    private final ExternalAchId externalAchId;
    private String title;
    private String description;
    private String imageUrl;

    public Achievement(AchievementId id, String title, String description, String imageUrl, ExternalAchId externalAchId) {
        this.id = id;
        this.externalAchId = externalAchId;
        setTitle(title);
        setDescription(description);
        setImageUrl(imageUrl);
    }

    public Achievement(String title, String description, String imageUrl, ExternalAchId externalAchId) {
        this(AchievementId.create(), title, description, imageUrl, externalAchId);
    }

    public void setTitle(String title) {
        if (title == null || title.trim().isEmpty() || title.isEmpty() || title.length() > 100)
            throw new IllegalArgumentException("The title provided is invalid.");

        this.title = title;
    }

    public void setDescription(String description) {
        if (description == null || description.trim().isEmpty() || description.isEmpty() || description.length() > 255)
            throw new IllegalArgumentException("The description provided is invalid.");

        this.description = description;
    }

    public void setImageUrl(String imageUrl) {
        if (imageUrl == null || imageUrl.trim().isEmpty() || imageUrl.isEmpty() || imageUrl.length() > 255)
            throw new IllegalArgumentException("The imageUrl provided is invalid.");

        this.imageUrl = imageUrl;
    }
}
