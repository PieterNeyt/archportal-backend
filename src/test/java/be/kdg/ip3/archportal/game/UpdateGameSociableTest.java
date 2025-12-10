package be.kdg.ip3.archportal.game;

import be.kdg.ip3.archportal.games.application.GameService;
import be.kdg.ip3.archportal.games.application.GameStudioService;
import be.kdg.ip3.archportal.games.api.dto.GameDto;
import be.kdg.ip3.archportal.games.domain.NotFoundException;
import be.kdg.ip3.archportal.games.domain.game.Game;
import be.kdg.ip3.archportal.games.domain.game.GameGenre;
import be.kdg.ip3.archportal.games.domain.game.GameId;
import be.kdg.ip3.archportal.games.domain.game.GameRepository;
import be.kdg.ip3.archportal.games.domain.gamestudio.GameStudio;
import be.kdg.ip3.archportal.games.domain.gamestudio.GameStudioId;
import be.kdg.ip3.archportal.games.domain.owner.OwnerId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.context.ApplicationEventPublisher;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
public class UpdateGameSociableTest {

    @Mock
    GameRepository gameRepository;
    @Mock
    GameStudioService gameStudioService;
    @Mock
    ApplicationEventPublisher applicationEventPublisher;

    GameService service;

    @BeforeEach
    void setUp() {
        service = new GameService(gameRepository, gameStudioService, applicationEventPublisher);
    }

    @Nested
    class UpdateGameTests {

        @Test
        void updateGame_existingGame_updatesAndSaves() {
            // Arrange
            var ownerId = new OwnerId(UUID.randomUUID());
            var gameId = new GameId(UUID.randomUUID());
            var studioId = new GameStudioId(UUID.randomUUID());

            var existingGame = spy(new Game(gameId, studioId, "Old Title", "Old Description",
                    BigDecimal.valueOf(10.0), "old-image.jpg", "old-url.com", GameGenre.ABSTRACT, 4));

            var gameStudio = mock(GameStudio.class);
            when(gameStudio.getId()).thenReturn(studioId);

            var gameDto = new GameDto(gameId.id(), "Title", "Description", "hello.com", "url.com",BigDecimal.valueOf(10.0), GameGenre.ABSTRACT, 4);


            when(gameRepository.findById(gameId.id())).thenReturn(Optional.of(existingGame));
            when(gameStudioService.findByOwnerId(ownerId)).thenReturn(gameStudio);

            // Act
            var result = service.updateGame(gameDto, ownerId);

            // Assert
            verify(existingGame).update(any(Game.class));
            verify(gameRepository).save(existingGame);
            assertThat(result).isEqualTo(existingGame);
        }

        @Test
        void updateGame_validGameDto_updatesFieldsCorrectly() {
            // Arrange
            var ownerId = new OwnerId(UUID.randomUUID());
            var gameId = new GameId(UUID.randomUUID());
            var studioId = new GameStudioId(UUID.randomUUID());

            var existingGame = spy(new Game(gameId, studioId, "Old Title", "Old Description",
                    BigDecimal.valueOf(10.0), "old-image.jpg", "old-url.com", GameGenre.ABSTRACT, 4));

            var gameStudio = mock(GameStudio.class);
            when(gameStudio.getId()).thenReturn(studioId);

            var gameDto = new GameDto(gameId.id(), "New Title", "New Description", "new-image.jpg", "new-url.com",BigDecimal.valueOf(20.0), GameGenre.ABSTRACT, 8);


            when(gameRepository.findById(gameId.id())).thenReturn(Optional.of(existingGame));
            when(gameStudioService.findByOwnerId(ownerId)).thenReturn(gameStudio);

            // Act
            service.updateGame(gameDto, ownerId);

            // Assert
            assertThat(existingGame.getTitle()).isEqualTo("New Title");
            assertThat(existingGame.getDescription()).isEqualTo("New Description");
            assertThat(existingGame.getPrice().money()).isEqualTo(BigDecimal.valueOf(20.0));
            assertThat(existingGame.getImageUrl()).isEqualTo("new-image.jpg");
            assertThat(existingGame.getGameUrl()).isEqualTo("new-url.com");
            assertThat(existingGame.getGenre()).isEqualTo(GameGenre.ABSTRACT);
            assertThat(existingGame.getMaxLobbySize()).isEqualTo(8);
        }
    }

    @Nested
    class ExceptionFlows {

        @Test
        void updateGame_gameNotFound_throwsNotFoundException() {
            // Arrange
            var gameId = new GameId(UUID.randomUUID());
            var ownerId = new OwnerId(UUID.randomUUID());

            var gameDto = new GameDto(gameId.id(), "Title", "Description", "hello.com", "url.com",BigDecimal.valueOf(10.0), GameGenre.ABSTRACT, 4);

            when(gameRepository.findById(gameId.id())).thenReturn(Optional.empty());

            // Act & Assert
            assertThatThrownBy(() -> service.updateGame(gameDto, ownerId))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessage("game not found");
        }

        @Test
        void updateGame_wrongGameStudio_throwsIllegalStateException() {
            // Arrange
            var ownerId = new OwnerId(UUID.randomUUID());
            var gameId = new GameId(UUID.randomUUID());
            var studioId = new GameStudioId(UUID.randomUUID());
            var wrongStudioId = new GameStudioId(UUID.randomUUID());

            var existingGame = spy(new Game(gameId, studioId, "Title", "Description",
                    BigDecimal.valueOf(10.0), "image.jpg", "url.com", GameGenre.ABSTRACT, 4));

            var gameStudio = mock(GameStudio.class);
            when(gameStudio.getId()).thenReturn(wrongStudioId);

            var gameDto = new GameDto(gameId.id(), "Title", "Description", "hello.com", "url.com",BigDecimal.valueOf(10.0), GameGenre.ABSTRACT, 4);


            when(gameRepository.findById(gameId.id())).thenReturn(Optional.of(existingGame));
            when(gameStudioService.findByOwnerId(ownerId)).thenReturn(gameStudio);

            // Act & Assert
            assertThatThrownBy(() -> service.updateGame(gameDto, ownerId))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessage("Wrong GameStudio");

            verify(gameRepository, never()).save(any());
        }

        @Test
        void updateGame_nullTitle_throwsIllegalArgumentException() {
            // Arrange
            var ownerId = new OwnerId(UUID.randomUUID());
            var gameId = new GameId(UUID.randomUUID());
            var studioId = new GameStudioId(UUID.randomUUID());

            var existingGame = spy(new Game(gameId, studioId, "Title", "Description",
                    BigDecimal.valueOf(10.0), "image.jpg", "url.com", GameGenre.ABSTRACT, 4));

            var gameStudio = mock(GameStudio.class);
            when(gameStudio.getId()).thenReturn(studioId);

            var gameDto = new GameDto(gameId.id(), null, "Description", "", "url.com",BigDecimal.valueOf(10.0), GameGenre.ABSTRACT, 4);

            when(gameRepository.findById(gameId.id())).thenReturn(Optional.of(existingGame));
            when(gameStudioService.findByOwnerId(ownerId)).thenReturn(gameStudio);

            // Act & Assert
            assertThatThrownBy(() -> service.updateGame(gameDto, ownerId))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("The title provided is invalid.");

            verify(gameRepository, never()).save(any());
        }

        @Test
        void updateGame_emptyDescription_throwsIllegalArgumentException() {
            // Arrange
            var ownerId = new OwnerId(UUID.randomUUID());
            var gameId = new GameId(UUID.randomUUID());
            var studioId = new GameStudioId(UUID.randomUUID());

            var existingGame = spy(new Game(studioId, "Title", "Description",
                    BigDecimal.valueOf(10.0), "image.jpg", "url.com", GameGenre.ABSTRACT, 4));

            var gameStudio = mock(GameStudio.class);
            when(gameStudio.getId()).thenReturn(studioId);

            var gameDto = new GameDto(gameId.id(), "Title", "", "", "url.com",BigDecimal.valueOf(10.0), GameGenre.ABSTRACT, 4);


            when(gameRepository.findById(gameId.id())).thenReturn(Optional.of(existingGame));
            when(gameStudioService.findByOwnerId(ownerId)).thenReturn(gameStudio);

            // Act & Assert
            assertThatThrownBy(() -> service.updateGame(gameDto, ownerId))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("The description provided is invalid.");

            verify(gameRepository, never()).save(any());
        }

        @Test
        void updateGame_negativePrice_throwsIllegalArgumentException() {
            // Arrange
            var ownerId = new OwnerId(UUID.randomUUID());
            var gameId = new GameId(UUID.randomUUID());
            var studioId = new GameStudioId(UUID.randomUUID());

            var existingGame = spy(new Game(studioId, "Title", "Description",
                    BigDecimal.valueOf(10.0), "image.jpg", "url.com", GameGenre.ABSTRACT, 4));

            var gameStudio = mock(GameStudio.class);
            when(gameStudio.getId()).thenReturn(studioId);

            var gameDto = new GameDto(gameId.id(), "Title", "Description", "", "url.com",BigDecimal.valueOf(-10.0), GameGenre.ABSTRACT, 4);


            when(gameRepository.findById(gameId.id())).thenReturn(Optional.of(existingGame));
            when(gameStudioService.findByOwnerId(ownerId)).thenReturn(gameStudio);

            // Act & Assert
            assertThatThrownBy(() -> service.updateGame(gameDto, ownerId))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("The price provided is invalid.");

            verify(gameRepository, never()).save(any());
        }

        @Test
        void updateGame_invalidGameUrl_throwsIllegalArgumentException() {
            // Arrange
            var ownerId = new OwnerId(UUID.randomUUID());
            var gameId = new GameId(UUID.randomUUID());
            var studioId = new GameStudioId(UUID.randomUUID());

            var existingGame = spy(new Game(studioId, "Title", "Description",
                    BigDecimal.valueOf(10.0), "image.jpg", "url.com", GameGenre.ABSTRACT, 4));

            var gameStudio = mock(GameStudio.class);
            when(gameStudio.getId()).thenReturn(studioId);

            var gameDto = new GameDto(gameId.id(), "Title", "Description", "Hello.com", "",BigDecimal.valueOf(10.0), GameGenre.ABSTRACT, 4);

            when(gameRepository.findById(gameId.id())).thenReturn(Optional.of(existingGame));
            when(gameStudioService.findByOwnerId(ownerId)).thenReturn(gameStudio);

            // Act & Assert
            assertThatThrownBy(() -> service.updateGame(gameDto, ownerId))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("The provided game url is invalid");

            verify(gameRepository, never()).save(any());
        }

        @Test
        void updateGame_nullGenre_throwsIllegalArgumentException() {
            // Arrange
            var ownerId = new OwnerId(UUID.randomUUID());
            var gameId = new GameId(UUID.randomUUID());
            var studioId = new GameStudioId(UUID.randomUUID());

            var existingGame = spy(new Game(studioId, "Title", "Description",
                    BigDecimal.valueOf(10.0), "image.jpg", "url.com", GameGenre.ABSTRACT, 4));

            var gameStudio = mock(GameStudio.class);
            when(gameStudio.getId()).thenReturn(studioId);

            var gameDto = new GameDto(gameId.id(), "Title", "Description", "", "url.com",BigDecimal.valueOf(10.0), null, 4);


            when(gameRepository.findById(gameId.id())).thenReturn(Optional.of(existingGame));
            when(gameStudioService.findByOwnerId(ownerId)).thenReturn(gameStudio);

            // Act & Assert
            assertThatThrownBy(() -> service.updateGame(gameDto, ownerId))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("The genre provided is invalid.");

            verify(gameRepository, never()).save(any());
        }

        @Test
        void updateGame_negativeMaxLobbySize_throwsIllegalArgumentException() {
            // Arrange
            var ownerId = new OwnerId(UUID.randomUUID());
            var gameId = new GameId(UUID.randomUUID());
            var studioId = new GameStudioId(UUID.randomUUID());

            var existingGame = spy(new Game(studioId, "Title", "Description",
                    BigDecimal.valueOf(10.0), "image.jpg", "url.com", GameGenre.ABSTRACT, 4));

            var gameStudio = mock(GameStudio.class);
            when(gameStudio.getId()).thenReturn(studioId);

            var gameDto = new GameDto(gameId.id(), "Title", "Description", "", "url.com",BigDecimal.valueOf(10.0), GameGenre.ABSTRACT, -4);


            when(gameRepository.findById(gameId.id())).thenReturn(Optional.of(existingGame));
            when(gameStudioService.findByOwnerId(ownerId)).thenReturn(gameStudio);

            // Act & Assert
            assertThatThrownBy(() -> service.updateGame(gameDto, ownerId))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("The provided lobby size is invalid");

            verify(gameRepository, never()).save(any());
        }

        @Test
        void updateGame_invalidImageUrl_throwsIllegalArgumentException() {
            // Arrange
            var ownerId = new OwnerId(UUID.randomUUID());
            var gameId = new GameId(UUID.randomUUID());
            var studioId = new GameStudioId(UUID.randomUUID());

            var existingGame = spy(new Game(studioId, "Title", "Description",
                    BigDecimal.valueOf(10.0), "image.jpg", "url.com", GameGenre.ABSTRACT, 4));

            var gameStudio = mock(GameStudio.class);
            when(gameStudio.getId()).thenReturn(studioId);

            var gameDto = new GameDto(gameId.id(), "Title", "Description", "", "url.com",BigDecimal.valueOf(10.0), GameGenre.ABSTRACT, 4);

            when(gameRepository.findById(gameId.id())).thenReturn(Optional.of(existingGame));
            when(gameStudioService.findByOwnerId(ownerId)).thenReturn(gameStudio);

            // Act & Assert
            assertThatThrownBy(() -> service.updateGame(gameDto, ownerId))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("The imageUrl provided is invalid.");

            verify(gameRepository, never()).save(any());
        }
    }
}