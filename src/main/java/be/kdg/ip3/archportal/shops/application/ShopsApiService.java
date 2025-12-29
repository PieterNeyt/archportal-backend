package be.kdg.ip3.archportal.shops.application;

import be.kdg.ip3.archportal.games.shared.GamesApi;
import be.kdg.ip3.archportal.profiles.domain.friendship.FriendshipRepository;
import be.kdg.ip3.archportal.profiles.domain.profile.ProfileRepository;
import be.kdg.ip3.archportal.shops.domain.benefit.BenefitId;
import be.kdg.ip3.archportal.shops.domain.benefit.BenefitRepository;
import be.kdg.ip3.archportal.shops.shared.GlobalBenefitDto;
import be.kdg.ip3.archportal.shops.shared.ShopsApi;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class ShopsApiService implements ShopsApi {
    private final BenefitRepository benefitRepository;

    public ShopsApiService(BenefitRepository benefitRepository) {
     this.benefitRepository = benefitRepository;
    }

    @Override
    public GlobalBenefitDto getBenefitById(UUID id) {
        var benefitId= new BenefitId(id);
        return benefitRepository.findById(benefitId)
                .map(GlobalBenefitDto::fromDomain)
                .orElseThrow(benefitId::notFound);
    }
}
