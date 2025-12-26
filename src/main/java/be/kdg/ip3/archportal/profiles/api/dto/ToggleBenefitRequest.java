package be.kdg.ip3.archportal.profiles.api.dto;

import java.util.UUID;

public record ToggleBenefitRequest(UUID benefitId, String type, String config, boolean active) {

}