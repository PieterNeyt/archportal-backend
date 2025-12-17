package be.kdg.ip3.archportal.profiles.api.dto;

import be.kdg.ip3.archportal.games.shared.GlobalGameDto;

public record LibraryGameDto(
        GlobalGameDto game,
        boolean favorite
) {}
