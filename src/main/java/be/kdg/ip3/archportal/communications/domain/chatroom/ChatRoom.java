package be.kdg.ip3.archportal.communications.domain.chatroom;

import lombok.Getter;
import org.jmolecules.ddd.annotation.AggregateRoot;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Getter
@AggregateRoot
public class ChatRoom {
    private final ChatRoomId id;
    private final Set<UUID> members;
    private final Set<Message> messages;

    public ChatRoom(ChatRoomId id, Set<UUID> members, Set<Message> messages) {
        this.id = id;
        this.members = members;
        this.messages = messages;
    }

    public ChatRoom() {
        this(new ChatRoomId(UUID.randomUUID()), new HashSet<>(), new HashSet<>());
    }

    public void addMember(UUID memberId) {
        if (members.contains(memberId))
            throw new IllegalArgumentException("Member already exists");
        if (memberId == null)
            throw new IllegalArgumentException("Member id is null");
        members.add(memberId);
    }
    
    public void addMembers(List<UUID> memberIds) {
        members.addAll(memberIds);
    }

    public void addMessage(Message message) {
        if (messages.contains(message))
            throw new IllegalArgumentException("Message already exists");
        if (message == null)
            throw new IllegalArgumentException("Message is null");
        messages.add(message);
    }
}
