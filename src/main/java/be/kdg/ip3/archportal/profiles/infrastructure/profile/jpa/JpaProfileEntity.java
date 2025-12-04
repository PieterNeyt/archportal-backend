package be.kdg.ip3.archportal.profiles.infrastructure.profile.jpa;


import be.kdg.ip3.archportal.profiles.domain.profile.Profile;
import be.kdg.ip3.archportal.profiles.domain.profile.ProfileId;
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

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String icon;

    @Column(nullable = false, unique = true)
    private String gamerTag;

    @Column(nullable = false)
    private int platformPoints;

    @ElementCollection
    @CollectionTable(name = "profile_friends", joinColumns = @JoinColumn(name = "profile_id"),schema = "profileservice")
    @Column(name = "friend_id", nullable = false)
    private List<UUID> friends = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "profile_platform_benefits", joinColumns = @JoinColumn(name = "profile_id"),schema = "profileservice")
    @Column(name = "benefit_id", nullable = false)
    private List<UUID> platformBenefits = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "profile_library", joinColumns = @JoinColumn(name = "profile_id"), schema = "profileservice")
    @Column(name = "game_id", nullable = false)
    private List<UUID> library = new ArrayList<>();

    public JpaProfileEntity() {}

    public JpaProfileEntity(UUID id, String firstName, String lastName ,String email, String icon, String gamerTag,
                            int platformPoints,  List<UUID> friends, List<UUID> platformBenefits, List<UUID> library) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.icon = icon;
        this.gamerTag = gamerTag;
        this.platformPoints = platformPoints;
        this.friends = friends;
        this.platformBenefits = platformBenefits;
        this.library = library;
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
                profile.getEmail(),
                profile.getIcon(),
                profile.getGamerTag(),
                profile.getPlatformPoints(),
                friendIds,
                profile.getPlatformBenefits(),
                profile.getLibrary()
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
                lastName,
                email,
                icon,
                gamerTag,
                friendIds,
                firstName,
                library
        );
    }
}