package be.kdg.ip3.archportal.gamestudio;

import be.kdg.ip3.archportal.games.application.GameStudioService;
import be.kdg.ip3.archportal.games.application.command.GameStudioCommand;
import be.kdg.ip3.archportal.games.domain.NotFoundException;
import be.kdg.ip3.archportal.games.domain.gamestudio.GameStudio;
import be.kdg.ip3.archportal.games.domain.gamestudio.GameStudioId;
import be.kdg.ip3.archportal.games.domain.gamestudio.GameStudioRepository;
import be.kdg.ip3.archportal.games.domain.owner.OwnerId;
import be.kdg.ip3.archportal.games.domain.owner.OwnerRepository;
import be.kdg.ip3.archportal.profiles.shared.ProfilesApi;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.AccessDeniedException;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
public class UpdateGameStudioSociableTest {

    @Mock
    GameStudioRepository gameStudioRepository;
    @Mock
    OwnerRepository ownerRepository;
    @Mock
    ProfilesApi profilesApi;
    @Mock
    ApplicationEventPublisher applicationEventPublisher;

    GameStudioService service;

    @BeforeEach
    void setUp() {
        service = new GameStudioService(gameStudioRepository, ownerRepository, profilesApi,applicationEventPublisher);
    }

    @Nested
    class UpdateGameStudioTests {

        @Test
        void updateGameStudio_existingStudio_updatesAndSaves() {
            // Arrange
            var ownerId = new OwnerId(UUID.randomUUID());
            var studioId = new GameStudioId(UUID.randomUUID());

            var existingStudio = mock(GameStudio.class);

            var command = mock(GameStudioCommand.class);
            when(command.id()).thenReturn(studioId);
            when(command.ownerId()).thenReturn(ownerId);

            var domainStudio = mock(GameStudio.class);
            when(command.toDomain()).thenReturn(domainStudio);

            when(gameStudioRepository.findById(studioId)).thenReturn(Optional.of(existingStudio));

            // Act
            var result = service.updateGameStudio(command);

            // Assert
            verify(existingStudio).update(domainStudio, ownerId);
            verify(gameStudioRepository).save(existingStudio);
            assertThat(result).isEqualTo(existingStudio);
        }

        @Test
        void updateGameStudio_validCommand_updatesFieldsCorrectly() {
            // Arrange
            var ownerId = new OwnerId(UUID.randomUUID());
            var studioId = new GameStudioId(UUID.randomUUID());

            var existingStudio = mock(GameStudio.class);

            var command = mock(GameStudioCommand.class);
            when(command.id()).thenReturn(studioId);
            when(command.ownerId()).thenReturn(ownerId);

            var domainStudio = mock(GameStudio.class);
            when(command.toDomain()).thenReturn(domainStudio);

            when(gameStudioRepository.findById(studioId)).thenReturn(Optional.of(existingStudio));

            // Act
            service.updateGameStudio(command);

            // Assert
            verify(existingStudio).update(domainStudio, ownerId);
        }
    }

    @Nested
    class ExceptionFlows {

        @Test
        void updateGameStudio_studioNotFound_throwsNotFoundException() {
            // Arrange
            var id = new GameStudioId(UUID.randomUUID());
            var command = mock(GameStudioCommand.class);
            when(command.id()).thenReturn(id);

            when(gameStudioRepository.findById(id)).thenReturn(Optional.empty());

            // Act & Assert
            assertThatThrownBy(() -> service.updateGameStudio(command))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessageContaining(id.id().toString());
        }

        @Test
        void updateGameStudio_wrongOwner_throwsAccessDeniedException() {
            // Arrange
            var id = new GameStudioId(UUID.randomUUID());
            var wrongOwner = new OwnerId(UUID.randomUUID());

            var command = mock(GameStudioCommand.class);
            when(command.id()).thenReturn(id);
            when(command.ownerId()).thenReturn(wrongOwner);

            var studio = mock(GameStudio.class);
            when(gameStudioRepository.findById(id)).thenReturn(Optional.of(studio));

            doThrow(new AccessDeniedException("This is not your game studio"))
                    .when(studio).update(any(), eq(wrongOwner));

            // Act & Assert
            assertThatThrownBy(() -> service.updateGameStudio(command))
                    .isInstanceOf(AccessDeniedException.class)
                    .hasMessage("This is not your game studio");

            verify(gameStudioRepository, never()).save(any());
        }


        @Test
        void updateGameStudio_nullIBAN_throwsIllegalArgumentException() {
            // Arrange
            var id = new GameStudioId(UUID.randomUUID());
            var ownerId = new OwnerId(UUID.randomUUID());

            var command = mock(GameStudioCommand.class);

            var invalidDomain = mock(GameStudio.class);
            when(invalidDomain.getIBAN()).thenReturn(null);
            when(invalidDomain.getName()).thenReturn("Valid Name");
            when(invalidDomain.getDescription()).thenReturn("Valid Description");

            when(command.id()).thenReturn(id);
            when(command.ownerId()).thenReturn(ownerId);
            when(command.toDomain()).thenReturn(invalidDomain);

            var studio = spy(new GameStudio(id, ownerId, "Studio", "Desc", "BE111"));
            when(gameStudioRepository.findById(id)).thenReturn(Optional.of(studio));

            // Act & Assert
            assertThatThrownBy(() -> service.updateGameStudio(command))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("The provided IBAN is empty");

            verify(gameStudioRepository, never()).save(any());
        }

    }
}
