package be.kdg.ip3.archportal.communications.domain.chatroom;

import be.kdg.ip3.archportal.communications.domain.NotFoundException;
import lombok.Getter;
import org.jmolecules.ddd.annotation.AggregateRoot;
import org.springframework.security.access.AccessDeniedException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@AggregateRoot
public class ChatRoom {
    private final ChatRoomId id;
    private String title;
    private final List<UUID> members;
    private final List<Message> messages;

    private static final int maxMembers = 10;


    public ChatRoom(ChatRoomId id, String title, List<UUID> members, List<Message> messages) {
        this.id = id;
        setTitle(title);
        this.members = members;
        this.messages = messages.stream().sorted(Comparator.comparing(Message::getTimestamp)).collect(Collectors.toList());
    }

    public ChatRoom() {
        this(new ChatRoomId(UUID.randomUUID()), "", new ArrayList<>(), new ArrayList<>());
    }

    public ChatRoom(String title) {
        this(new ChatRoomId(UUID.randomUUID()), title, new ArrayList<>(), new ArrayList<>());
    }

    public void addMember(UUID memberId) {
        if (members.size() >= maxMembers)
            throw new AccessDeniedException("Cannot add more than " + maxMembers + " members");
        if (members.contains(memberId))
            throw new IllegalArgumentException("Member already exists");
        if (memberId == null)
            throw new IllegalArgumentException("Member id is null");
        members.add(memberId);
    }

    public void addMembers(List<UUID> memberIds) {
        if (members == null)
            throw new IllegalArgumentException("Member list is null");

        if (memberIds.contains(null))
            throw new IllegalArgumentException("Member list containers null members");

        if (members.size() + memberIds.size() > maxMembers)
            throw new AccessDeniedException("Cannot add more than " + maxMembers + " members");

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

    public static void validateFriends(List<UUID> allProfiles, List<UUID> friendProfiles) {
        var notFriends = allProfiles.stream().filter(p -> !friendProfiles.contains(p)).toList();
        if (!notFriends.isEmpty()) {
            throw new IllegalArgumentException("You are not friends with the following gamerTags: " + notFriends);
        }
    }

    public void validateMember(UUID profileId) {
        if (!members.contains(profileId))
            throw new AccessDeniedException("You are not a member of this chat");
    }

    private void setTitle(String title) {
        if (title == null || title.isBlank())
            throw new IllegalArgumentException("Title is null or empty");
        if (title.length() > 100)
            throw new IllegalArgumentException("Title is too long");
        this.title = title;
    }

    public void leave(UUID profileId) {
        validateMember(profileId);
        members.remove(profileId);
    }
}
