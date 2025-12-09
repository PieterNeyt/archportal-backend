package be.kdg.ip3.archportal.communications.infrastructure.chatroom.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface JpaChatRoomRepository extends JpaRepository<JpaChatRoomEntity, UUID> {
    @Query("select c from JpaChatRoomEntity c join c.members m where m = :profileId")
    List<JpaChatRoomEntity> findChatRoomsOfProfileId(@Param("profileId") UUID profileId);
}
