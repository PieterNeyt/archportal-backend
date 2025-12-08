package be.kdg.ip3.archportal.communications.infrastructure.chatroom.jpa;

import be.kdg.ip3.archportal.communications.domain.chatroom.ChatRoom;
import be.kdg.ip3.archportal.communications.domain.chatroom.ChatRoomId;
import jakarta.persistence.*;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity
@Table(name = "chat_room", schema = "communicationservice")
public class JpaChatRoomEntity {
    @Id
    private UUID id;
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "chat_room_member", schema = "communicationservice",
            joinColumns = @JoinColumn(name = "char_room_id"))
    private Set<UUID> members;
    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<JpaMessageEntity> messages;

    protected JpaChatRoomEntity() {
    }

    public JpaChatRoomEntity(UUID id, Set<UUID> members, Set<JpaMessageEntity> messages) {
        this.id = id;
        this.members = members;
        this.messages = messages;
    }

    public static JpaChatRoomEntity fromDomain(ChatRoom chatRoom) {
        return new JpaChatRoomEntity(
                chatRoom.getId().id(),
                chatRoom.getMembers(),
                chatRoom.getMessages().stream().map(JpaMessageEntity::fromDomain).collect(Collectors.toSet())
        );
    }

    public ChatRoom toDomain() {
        return new ChatRoom(
                new ChatRoomId(id),
                members,
                messages.stream().map(JpaMessageEntity::toDomain).collect(Collectors.toSet())
        );
    }
}
