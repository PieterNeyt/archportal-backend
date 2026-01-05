package be.kdg.ip3.archportal.communications.chatroom;

import be.kdg.ip3.archportal.communications.application.ChatRoomApiService;
import be.kdg.ip3.archportal.communications.domain.chatroom.ChatRoom;
import be.kdg.ip3.archportal.communications.domain.chatroom.ChatRoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ChatRoomApiServiceSociableTest {
    @Mock
    private ChatRoomRepository chatRoomRepository;
    private ChatRoomApiService apiService;

    @BeforeEach
    void setUp() {
        apiService = new ChatRoomApiService(chatRoomRepository);
    }

    @Test
    void createChatRoom_ShouldCreateWithHostAndReturnId() {
        var hostId = UUID.randomUUID();
        var title = "Private Chat";

        var id = apiService.createChatRoom(hostId, title);

        var captor = ArgumentCaptor.forClass(ChatRoom.class);
        verify(chatRoomRepository).save(captor.capture());

        assertThat(id).isNotNull();
        assertThat(captor.getValue().getTitle()).isEqualTo(title);
        assertThat(captor.getValue().getMembers()).contains(hostId);
    }
}
