package be.kdg.ip3.archportal.communications.infrastructure.chatroom.jpa;

import be.kdg.ip3.archportal.communications.domain.chatroom.ChatRoom;
import be.kdg.ip3.archportal.communications.domain.chatroom.ChatRoomId;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity
@Table(name = "chat_room", schema = "communicationservice")
public class JpaChatRoomEntity {
    @Id
    private UUID id;
    @Column(nullable = false, length = 100)
    private String title;
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "chat_room_member", schema = "communicationservice",
            joinColumns = @JoinColumn(name = "chat_room_id"))
    private Set<UUID> members;
    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<JpaMessageEntity> messages;

    protected JpaChatRoomEntity() {
    }

    public JpaChatRoomEntity(UUID id, String title, Set<UUID> members, Set<JpaMessageEntity> messages) {
        this.id = id;
        this.title = title;
        this.members = members;
        setMessages(messages);
    }

    public static JpaChatRoomEntity fromDomain(ChatRoom chatRoom) {
        return new JpaChatRoomEntity(
                chatRoom.getId().id(),
                chatRoom.getTitle(),
                new HashSet<>(chatRoom.getMembers()),
                chatRoom.getMessages().stream().map(JpaMessageEntity::fromDomain).collect(Collectors.toSet())
        );
    }

    public ChatRoom toDomain() {
        return new ChatRoom(
                new ChatRoomId(id),
                title,
                new ArrayList<>(members),
                messages.stream().map(JpaMessageEntity::toDomain).toList()
        );
    }

    private void setMessages(Set<JpaMessageEntity> messages) {
        this.messages = messages;
        this.messages.forEach(m -> m.setChatRoom(this));
    }
}
