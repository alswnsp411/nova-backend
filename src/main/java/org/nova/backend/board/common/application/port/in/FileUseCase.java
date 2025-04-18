package org.nova.backend.board.common.application.port.in;

import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.nova.backend.board.common.application.dto.response.FileResponse;
import org.nova.backend.board.common.domain.model.entity.File;
import org.nova.backend.board.common.domain.model.valueobject.PostType;
import org.springframework.web.multipart.MultipartFile;

public interface FileUseCase {
    void deleteFileById(UUID fileId, UUID memberId);
    void downloadFile(UUID fileId, HttpServletResponse response, UUID memberId);
    void deleteFiles(List<UUID> fileIds);
    Optional<File> findFileById(UUID fileId);
    List<File> findFilesByIds(List<UUID> fileIds);
    List<FileResponse> uploadFiles(List<MultipartFile> files, UUID memberId , PostType postType);
}

