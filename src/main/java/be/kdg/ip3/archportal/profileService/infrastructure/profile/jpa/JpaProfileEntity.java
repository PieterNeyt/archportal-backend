package be.kdg.ip3.archportal.profileService.infrastructure.profile.jpa;


import be.kdg.ip3.archportal.profileService.domain.profile.Profile;
import be.kdg.ip3.archportal.profileService.domain.profile.ProfileId;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Table(name = "profile", schema = "profileservice")
public class JpaProfileEntity {

    @Id
    private UUID id;

    @Column(nullable = false, length = 255)
    private String firstName;

    @Column(nullable = false, length = 255)
    private String lastName;

    @Column(nullable = true, length = 255)
    private String icon;

    @Column(nullable = false, length = 255)
    private String gamerTag;

    @Column(nullable = false)
    private UUID libraryId;

    @Column(nullable = false)
    private int platformPoints;

    @ElementCollection
    @CollectionTable(name = "profile_friends", joinColumns = @JoinColumn(name = "profile_id"))
    @Column(name = "friend_id", nullable = false)
    private List<UUID> friends = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "profile_platform_benefits", joinColumns = @JoinColumn(name = "profile_id"))
    @Column(name = "benefit_id", nullable = false)
    private List<UUID> platformBenefits = new ArrayList<>();

    // No-arg constructor voor JPA
    public JpaProfileEntity() {}

    public JpaProfileEntity(UUID id, String firstName, String lastName, String icon, String gamerTag,
                            UUID libraryId, int platformPoints, List<UUID> friends, List<UUID> platformBenefits) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.icon = icon;
        this.gamerTag = gamerTag;
        this.libraryId = libraryId;
        this.platformPoints = platformPoints;
        this.friends = friends;
        this.platformBenefits = platformBenefits;
    }

    public JpaProfileEntity(String firstName, String lastName, String icon, String gamerTag,
                            UUID libraryId, int platformPoints, List<UUID> friends, List<UUID> platformBenefits) {
        this(ProfileId.create().id(), firstName, lastName, icon, gamerTag, libraryId, platformPoints, friends, platformBenefits);
    }

    public static JpaProfileEntity fromDomain(Profile profile) {
        List<UUID> friendIds = new ArrayList<>();
        for (ProfileId f : profile.getFriends()) {
            friendIds.add(f.id());
        }
        return new JpaProfileEntity(
                profile.getId().id(),
                profile.getFirstName(),
                profile.getLastName(),
                profile.getIcon(),
                profile.getGamerTag(),
                profile.getLibraryId(),
                profile.getPlatformPoints(),
                friendIds,
                profile.getPlatformBenefits()
        );
    }

    public Profile toDomain() {
        List<ProfileId> friendIds = new ArrayList<>();
        for (UUID f : friends) {
            friendIds.add(new ProfileId(f));
        }
        return new Profile(
                new ProfileId(id),
                platformBenefits,
                platformPoints,
                libraryId,
                lastName,
                icon,
                gamerTag,
                friendIds,
                firstName
        );
    }
}