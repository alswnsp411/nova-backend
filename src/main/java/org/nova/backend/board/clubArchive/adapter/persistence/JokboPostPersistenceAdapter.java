package org.nova.backend.board.clubArchive.adapter.persistence;

import jakarta.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.nova.backend.board.common.domain.model.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.nova.backend.board.clubArchive.application.port.out.JokboPostPersistencePort;
import org.nova.backend.board.clubArchive.domain.model.entity.JokboPost;
import org.nova.backend.board.clubArchive.adapter.persistence.repository.JokboPostRepository;
import org.nova.backend.board.clubArchive.domain.model.valueobject.Semester;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JokboPostPersistenceAdapter implements JokboPostPersistencePort {
    private final JokboPostRepository jokboPostRepository;

    @Override
    @Transactional
    public JokboPost save(JokboPost jokboPost) {
        JokboPost savedJokboPost = jokboPostRepository.save(jokboPost);
        jokboPostRepository.flush();
        return savedJokboPost;
    }

    @Override
    public Optional<JokboPost> findByPostId(UUID postId) {
        return jokboPostRepository.findByPostId(postId);
    }

    @Override
    public Optional<JokboPost> findByPost(Post post) {
        return jokboPostRepository.findByPostId(post.getId());
    }

    @Override
    @Transactional
    public void deleteByPost(Post post) {
        jokboPostRepository.deleteByPost(post);
    }

    /**
     * 교수명, 학년, 학기별 필터링 가능
     */
    @Override
    public Page<JokboPost> findPostsByFilter(UUID boardId, String professorName, Integer year, Semester semester, Pageable pageable) {
        return jokboPostRepository.findByFilter(boardId, professorName, year, semester, pageable);
    }
}
