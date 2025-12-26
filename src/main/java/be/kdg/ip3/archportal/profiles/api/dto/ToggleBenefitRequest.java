package be.kdg.ip3.archportal.profiles.api.dto;

import be.kdg.ip3.archportal.profiles.domain.benefit.ProfileBenefitType;

import java.util.UUID;

public record ToggleBenefitRequest(UUID benefitId, ProfileBenefitType type, String config, boolean active) {

}