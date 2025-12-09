package be.kdg.ip3.archportal.communications.domain.chatroom;

import be.kdg.ip3.archportal.communications.domain.NotFoundException;
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
    private String title;
    private final Set<UUID> members;
    private final Set<Message> messages;

    public ChatRoom(ChatRoomId id, String title, Set<UUID> members, Set<Message> messages) {
        this.id = id;
        this.title = title;
        this.members = members;
        this.messages = messages;
    }

    public ChatRoom() {
        this(new ChatRoomId(UUID.randomUUID()), "Group", new HashSet<>(), new HashSet<>());
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
    
    public static void validateProfiles(List<String> allTags, List<String> foundTags) {
        var missing = allTags.stream().filter(tag -> !foundTags.contains(tag)).toList();
        if (!missing.isEmpty()) {
            throw new NotFoundException("The following gamerTags do not exist: " + missing);
        }
    }
    
    public static void validateFriends(List<UUID> allProfiles, List<UUID> friendProfiles){
        var notFriends = allProfiles.stream().filter(p -> !friendProfiles.contains(p)).toList();
        if (!notFriends.isEmpty()) {
            throw new IllegalArgumentException("You are not friends with the following gamerTags: " + notFriends);
        }
    }
}
