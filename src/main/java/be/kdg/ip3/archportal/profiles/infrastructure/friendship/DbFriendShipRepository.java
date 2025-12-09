package be.kdg.ip3.archportal.profiles.infrastructure.friendship;

import be.kdg.ip3.archportal.profiles.domain.friendship.Friendship;
import be.kdg.ip3.archportal.profiles.domain.friendship.FriendshipRepository;
import be.kdg.ip3.archportal.profiles.domain.profile.ProfileId;
import be.kdg.ip3.archportal.profiles.infrastructure.friendship.jpa.JpaFriendShipRepository;
import be.kdg.ip3.archportal.profiles.infrastructure.friendship.jpa.JpaFriendshipEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class DbFriendShipRepository implements FriendshipRepository {
    private final JpaFriendShipRepository repository;

    public DbFriendShipRepository(JpaFriendShipRepository repository) {
        this.repository = repository;
    }

    @Override
    public void save(Friendship friendship) {
        repository.save(JpaFriendshipEntity.fromDomain(friendship));
    }

    @Override
    public void delete(Friendship friendship) {
        repository.delete(JpaFriendshipEntity.fromDomain(friendship));
    }

    @Override
    public boolean existsBetween(ProfileId profileAId, ProfileId profileBId) {
        return repository.existsBetween(profileAId.id(), profileBId.id());
    }

    @Override
    public Optional<Friendship> findBetween(ProfileId profileAId, ProfileId profileBId) {
        return repository.findBetween(profileAId.id(), profileBId.id()).map(JpaFriendshipEntity::toDomain);
    }

    @Override
    public List<UUID> findFriendIds(ProfileId creatorId, List<ProfileId> otherIds) {
        var ids = otherIds.stream().map(ProfileId::id).toList();
        return repository.findFriendsWithCreator(creatorId.id(), ids).stream()
                .map(e -> e.getProfileAId().equals(creatorId.id()) ? e.getProfileBId() : e.getProfileAId())
                .toList();
    }
}
