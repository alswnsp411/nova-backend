package org.nova.backend.board.adapter.persistence.repository;

import java.util.Optional;
import java.util.UUID;
import org.nova.backend.board.domain.model.entity.Board;
import org.nova.backend.board.domain.model.valueobject.BoardCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board, UUID> {
    Optional<Board> findByCategory(BoardCategory category);
}

