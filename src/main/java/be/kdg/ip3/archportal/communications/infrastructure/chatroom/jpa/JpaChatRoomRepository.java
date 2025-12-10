package be.kdg.ip3.archportal.communications.infrastructure.chatroom.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JpaChatRoomRepository extends JpaRepository<JpaChatRoomEntity, UUID> {
    @Query("SELECT cr FROM JpaChatRoomEntity cr " +
            "LEFT JOIN FETCH cr.messages m " +
            "WHERE cr.id = :id " +
            "AND (m.timestamp = (SELECT MAX(m2.timestamp) FROM JpaMessageEntity m2 WHERE m2.chatRoom = cr) " +
            "     OR m.id IS NULL)")
    Optional<JpaChatRoomEntity> findByIdWithLastMessage(@Param("id") UUID id);


    @Query("""
            SELECT c
            FROM JpaChatRoomEntity c
            JOIN c.members m
            LEFT JOIN FETCH c.messages me
            WHERE m = :profileId
            AND (me.timestamp = (SELECT MAX (m2.timestamp) FROM JpaMessageEntity m2 WHERE m2.chatRoom = c) OR me.id IS NULL)
            """)
    List<JpaChatRoomEntity> findChatRoomsOfProfileIdWithLastMessage(@Param("profileId") UUID profileId);
}
