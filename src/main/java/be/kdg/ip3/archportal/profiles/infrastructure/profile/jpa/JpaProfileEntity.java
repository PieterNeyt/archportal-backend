package be.kdg.ip3.archportal.profiles.infrastructure.profile.jpa;

import be.kdg.ip3.archportal.profiles.domain.Library.Game;
import be.kdg.ip3.archportal.profiles.domain.friendRequest.FriendRequest;
import be.kdg.ip3.archportal.profiles.domain.profile.Profile;
import be.kdg.ip3.archportal.profiles.domain.profile.ProfileId;
import be.kdg.ip3.archportal.profiles.infrastructure.friendRequest.jpa.JpaFriendRequest;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.*;
import java.util.stream.Collectors;

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

    @Column
    private String icon;
    @Column
    private String originalIcon;

    @Column(nullable = false, unique = true)
    private String gamerTag;

    @Column(nullable = false)
    private int platformPoints;

    @Column
    private UUID activeProfilePictureId;

    @Column
    private UUID activeUsernameColorId;

    @ElementCollection
    @CollectionTable(name = "profile_platform_benefits",
            joinColumns = @JoinColumn(name = "profile_id"),
            schema = "profileservice")
    @Column(name = "benefit_id", nullable = false)
    private Set<UUID> platformBenefits = new HashSet<>();


    @ElementCollection
    @CollectionTable(name = "profile_library", joinColumns = @JoinColumn(name = "profile_id"), schema = "profileservice")
    private List<JpaGameEntity> games = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "friend_requests", schema = "profileservice", joinColumns = @JoinColumn(name = "receiver_id"),
            uniqueConstraints = @UniqueConstraint(
                    name = "uq_friend_request_sender_receiver",
                    columnNames = {"sender_id", "receiver_id"}
            ))
    private Set<JpaFriendRequest> incomingRequests = new HashSet<>();

    public JpaProfileEntity() {
    }

    public JpaProfileEntity(UUID id, String firstName, String lastName, String email, String icon, String gamerTag,
                            int platformPoints, Set<UUID> platformBenefits, List<JpaGameEntity> library,
                            Set<JpaFriendRequest> incomingRequests, String originalIcon, UUID activeProfilePictureId, UUID activeUsernameColorId) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.icon = icon;
        this.gamerTag = gamerTag;
        this.platformPoints = platformPoints;
        this.platformBenefits = platformBenefits;
        this.games = library;
        this.incomingRequests = incomingRequests;
        this.originalIcon = originalIcon;
        this.activeProfilePictureId = activeProfilePictureId;
        this.activeUsernameColorId = activeUsernameColorId;
    }

    public static JpaProfileEntity fromDomain(Profile profile) {
        Set<JpaFriendRequest> incoming = profile.getIncomingFriendRequests().stream()
                .map(JpaFriendRequest::from)
                .collect(Collectors.toSet());

        List<JpaGameEntity> jpaLibrary = profile.getGames().stream()
                .map(games -> new JpaGameEntity(games.getGameId(), games.isFavorite()))
                .collect(Collectors.toList());

        return new JpaProfileEntity(
                profile.getId().id(),
                profile.getFirstName(),
                profile.getLastName(),
                profile.getEmail(),
                profile.getIcon(),
                profile.getGamerTag(),
                profile.getPlatformPoints(),
                profile.getPlatformBenefits(),
                jpaLibrary,
                incoming,
                profile.getOriginalIcon(),
                profile.getActiveProfilePictureId(),
                profile.getActiveUsernameColorId()
        );
    }

    public Profile toDomain() {
        Set<FriendRequest> incoming = incomingRequests.stream()
                .map(JpaFriendRequest::toDomain)
                .collect(Collectors.toSet());

        List<Game> domainGames = games.stream()
                .map(JpaGameEntity::toDomain)
                .collect(Collectors.toList());

        return new Profile(
                new ProfileId(id),
                platformBenefits,
                platformPoints,
                lastName,
                email,
                icon,
                gamerTag,
                firstName,
                domainGames,
                incoming,
                originalIcon,
                activeProfilePictureId,
                activeUsernameColorId
        );
    }
}