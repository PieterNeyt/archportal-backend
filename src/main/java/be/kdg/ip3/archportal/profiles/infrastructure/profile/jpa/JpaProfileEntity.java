package be.kdg.ip3.archportal.profiles.infrastructure.profile.jpa;

import be.kdg.ip3.archportal.profiles.domain.Library.Game;
import be.kdg.ip3.archportal.profiles.domain.friendRequest.FriendRequest;
import be.kdg.ip3.archportal.profiles.domain.profile.Profile;
import be.kdg.ip3.archportal.profiles.domain.profile.ProfileId;
import be.kdg.ip3.archportal.profiles.domain.profile.Section;
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

    private String icon;

    @Column(nullable = false, unique = true)
    private String gamerTag;

    @Column(nullable = false)
    private int platformPoints;

    @ElementCollection
    @CollectionTable(
            name = "profile_platform_benefits",
            schema = "profileservice",
            joinColumns = @JoinColumn(name = "profile_id")
    )
    @Column(name = "benefit_id", nullable = false)
    private List<UUID> platformBenefits = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "profile_library", schema = "profileservice", joinColumns = @JoinColumn(name = "profile_id"))
    private List<JpaGameEntity> games = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "friend_requests", schema = "profileservice", joinColumns = @JoinColumn(name = "receiver_id"),
            uniqueConstraints = @UniqueConstraint(
                    name = "uq_friend_request_sender_receiver",
                    columnNames = {"sender_id", "receiver_id"}
            )
    )
    private Set<JpaFriendRequest> incomingRequests = new HashSet<>();

    @ElementCollection
    @CollectionTable(name = "profile_sections", schema = "profileservice", joinColumns = @JoinColumn(name = "profile_id"))
    private List<JpaSectionEntity> sections = new ArrayList<>();

    protected JpaProfileEntity() {}

    public JpaProfileEntity(
            UUID id,
            String firstName,
            String lastName,
            String email,
            String icon,
            String gamerTag,
            int platformPoints,
            List<UUID> platformBenefits,
            List<JpaGameEntity> games,
            Set<JpaFriendRequest> incomingRequests,
            List<JpaSectionEntity> sections
    ) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.icon = icon;
        this.gamerTag = gamerTag;
        this.platformPoints = platformPoints;
        this.platformBenefits = platformBenefits;
        this.games = games;
        this.incomingRequests = incomingRequests;
        this.sections = sections;
    }

    public static JpaProfileEntity fromDomain(Profile profile) {

        Set<JpaFriendRequest> incoming = profile.getIncomingFriendRequests().stream()
                .map(JpaFriendRequest::from)
                .collect(Collectors.toSet());

        List<JpaGameEntity> jpaGames = profile.getGames().stream()
                .map(game -> new JpaGameEntity(game.getGameId(), game.isFavorite()))
                .toList();

        List<JpaSectionEntity> jpaSections = profile.getSections().stream()
                .map(JpaSectionEntity::fromDomain)
                .toList();

        return new JpaProfileEntity(
                profile.getId().id(),
                profile.getFirstName(),
                profile.getLastName(),
                profile.getEmail(),
                profile.getIcon(),
                profile.getGamerTag(),
                profile.getPlatformPoints(),
                profile.getPlatformBenefits(),
                jpaGames,
                incoming,
                jpaSections
        );
    }

    public Profile toDomain() {

        Set<FriendRequest> incoming = incomingRequests.stream()
                .map(JpaFriendRequest::toDomain)
                .collect(Collectors.toSet());

        List<Game> domainGames = games.stream()
                .map(JpaGameEntity::toDomain)
                .collect(Collectors.toList());

        List<Section> domainSections = sections.stream()
                .map(JpaSectionEntity::toDomain)
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
                domainSections
        );
    }
}
