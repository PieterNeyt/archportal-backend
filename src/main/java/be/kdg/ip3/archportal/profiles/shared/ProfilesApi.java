package be.kdg.ip3.archportal.profiles.shared;

import org.springframework.modulith.NamedInterface;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@NamedInterface
public interface ProfilesApi {
    void addGamesToLibrary(UUID profileId, List<UUID> games);
    void checkAlreadyOwnsGames(UUID profileId, List<UUID> games);
    void checkAlreadyOwnsGame(UUID profileId, UUID  gameId);
    boolean existsById(UUID id);
    List<ProfileDto> getProfilesFromGamerTags(List<String> gamerTags);
    UUID getActiveUsernameColorId(UUID profileId);
    UUID getProfileFromGamerTag(String gamerTag);
    boolean areFriends(UUID profileAId, UUID profileBId);

    List<UUID> getFriendIdsWithCreator(UUID creatorId, List<UUID> candidateIds);

    String getProfileEmail(UUID id);
    List<BasicProfileInfo> getBasicProfiles(List<UUID> profileIds);
    
    List<ProfileDto> getAllFriends(UUID profileId);
    Set<UUID> getProfileBenefitsByProfileId(UUID profileId);

    String getProfileGamerTag(UUID id);

    int addBenefitToProfile(UUID profileId,UUID benefitId,int pointsCost);
    void removeBenefitFromProfile(UUID profileId, UUID benefitId);
}

