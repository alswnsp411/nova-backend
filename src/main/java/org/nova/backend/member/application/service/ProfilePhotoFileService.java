package org.nova.backend.member.application.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.nova.backend.board.common.application.service.FileService;
import org.nova.backend.board.common.domain.exception.FileDomainException;
import org.nova.backend.board.util.FileUtil;
import org.nova.backend.member.adapter.repository.ProfilePhotoFileRepository;
import org.nova.backend.member.application.dto.response.ProfilePhotoResponse;
import org.nova.backend.member.application.mapper.MemberProfilePhotoMapper;
import org.nova.backend.member.domain.exception.ProfilePhotoFileDomainException;
import org.nova.backend.member.domain.model.entity.ProfilePhoto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProfilePhotoFileService {
    private static final Logger logger = LoggerFactory.getLogger(ProfilePhotoFileService.class);

    private final FileService fileService;
    private final ProfilePhotoFileRepository profilePhotoFileRepository;
    private final MemberProfilePhotoMapper memberProfilePhotoMapper;

    @Value("${file.storage.path}")
    private String baseFileStoragePath;

    /**
     * 프로필 사진 업로드
     */
    @Transactional
    public ProfilePhotoResponse uploadProfilePhoto(
            MultipartFile profilePhoto
    ) {
        if (profilePhoto == null || profilePhoto.isEmpty()) {
            throw new FileDomainException("업로드할 파일이 없습니다.");
        }

        FileUtil.validateImageFile(profilePhoto);
        FileUtil.validateFileSize(profilePhoto);

        String storagePath = Paths.get(baseFileStoragePath, "profile").toString();

        return processProfilePhotoUpload(profilePhoto, storagePath);
    }

    /**
     * 로컬에서 파일 삭제 + DB에서도 삭제
     */
    @Transactional
    public void deleteProfilePhotoById(UUID profilePhotoId) {
        ProfilePhoto profilePhotoToDelete = findProfilePhotoById(profilePhotoId);

        Path filePath = Paths.get(profilePhotoToDelete.getFilePath());
        try {
            Files.deleteIfExists(filePath);
            logger.info("파일 삭제 성공: {}", profilePhotoToDelete.getFilePath());
        } catch (IOException e) {
            logger.error("파일 삭제 실패: {}", profilePhotoToDelete.getFilePath(), e);
        }

        profilePhotoFileRepository.deleteProfilePhotoById(profilePhotoId);
    }

    /**
     * 특정 파일 조회
     */
    public ProfilePhoto findProfilePhotoById(UUID profilePhotoId) {
        return profilePhotoFileRepository.findProfilePhotoById(profilePhotoId)
                .orElseThrow(() -> new ProfilePhotoFileDomainException("프로필 사진을 찾을 수 없습니다.", HttpStatus.NOT_FOUND));
    }


    /**
     * 파일 업로드 처리
     */
    private ProfilePhotoResponse processProfilePhotoUpload(
            MultipartFile file,
            String storagePath
    ) {
        String savedFilePath = fileService.saveFileToLocal(file, storagePath);
        ProfilePhoto savedProfilePhoto = new ProfilePhoto(null, file.getOriginalFilename(), savedFilePath);
        savedProfilePhoto = profilePhotoFileRepository.save(savedProfilePhoto);

        return memberProfilePhotoMapper.toResponse(savedProfilePhoto);
    }

}
