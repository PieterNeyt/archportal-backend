package be.kdg.ip3.archportal.profiles.infrastructure.profile.jpa;

import be.kdg.ip3.archportal.profiles.domain.profile.Section;
import be.kdg.ip3.archportal.profiles.domain.profile.SectionType;
import be.kdg.ip3.archportal.profiles.domain.profile.Visibility;
import jakarta.persistence.*;
import lombok.Getter;

@Embeddable
@Getter
public class JpaSectionEntity {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SectionType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Visibility visibility;

    protected JpaSectionEntity() {
    }

    public JpaSectionEntity(SectionType type, Visibility visibility) {
        this.type = type;
        this.visibility = visibility;
    }

    public static JpaSectionEntity fromDomain(Section section) {
        return new JpaSectionEntity(
                section.getType(),
                section.getVisibility()
        );
    }

    public Section toDomain() {
        return new Section(type, visibility);
    }
}
