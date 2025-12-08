package be.kdg.ip3.archportal.communications.infrastructure.chatroom.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaChatRoomRepository extends JpaRepository<JpaChatRoomEntity, UUID> {
}
