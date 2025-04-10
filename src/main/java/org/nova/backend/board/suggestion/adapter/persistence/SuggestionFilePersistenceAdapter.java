package org.nova.backend.board.suggestion.adapter.persistence;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.nova.backend.board.suggestion.adapter.persistence.repository.SuggestionFileRepository;
import org.nova.backend.board.suggestion.application.port.out.SuggestionFilePersistencePort;
import org.nova.backend.board.suggestion.domain.model.entity.SuggestionFile;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SuggestionFilePersistenceAdapter implements SuggestionFilePersistencePort {
    private final SuggestionFileRepository fileRepository;

    @Override
    public SuggestionFile save(SuggestionFile file) {
        return fileRepository.save(file);
    }

    @Override
    public List<SuggestionFile> findFilesByIds(List<UUID> fileIds) {
        return fileRepository.findAllById(fileIds);
    }

    @Override
    public Optional<SuggestionFile> findFileById(UUID fileId) {
        return fileRepository.findById(fileId);
    }
}
