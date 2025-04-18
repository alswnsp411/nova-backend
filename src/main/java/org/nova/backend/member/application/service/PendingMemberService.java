package org.nova.backend.member.application.service;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.nova.backend.member.adapter.repository.GraduationRepository;
import org.nova.backend.member.adapter.repository.MemberRepository;
import org.nova.backend.member.adapter.repository.PendingMemberRepository;
import org.nova.backend.member.application.dto.response.PendingGraduationResponse;
import org.nova.backend.member.application.dto.response.PendingMemberDetailResponse;
import org.nova.backend.member.application.dto.response.PendingMemberResponse;
import org.nova.backend.member.application.mapper.GraduationMapper;
import org.nova.backend.member.application.mapper.MemberMapper;
import org.nova.backend.member.application.mapper.PendingGraduationMapper;
import org.nova.backend.member.application.mapper.PendingMemberMapper;
import org.nova.backend.member.domain.exception.PendingMemberDomainException;
import org.nova.backend.member.domain.model.entity.Graduation;
import org.nova.backend.member.domain.model.entity.Member;
import org.nova.backend.member.domain.model.entity.PendingGraduation;
import org.nova.backend.member.domain.model.entity.PendingMember;
import org.nova.backend.member.domain.model.entity.ProfilePhoto;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PendingMemberService {

    private final PendingMemberRepository pendingMemberRepository;

    private final MemberRepository memberRepository;
    private final GraduationRepository graduationRepository;

    private final ProfilePhotoFileService profilePhotoFileService;

    private final MemberMapper memberMapper;
    private final GraduationMapper graduationMapper;
    private final PendingMemberMapper pendingMemberMapper;
    private final PendingGraduationMapper pendingGraduationMapper;

    /**
     * 응답 객체 생성
     *
     * @param pendingMember
     * @return PendingMemberResponse
     */
    public PendingMemberResponse getPendingMemberResponseFromPendingMember(PendingMember pendingMember) {
        ProfilePhoto profilePhoto = profilePhotoFileService.getProfilePhoto(pendingMember.getProfilePhoto());
        return pendingMemberMapper.toResponse(pendingMember, profilePhoto);
    }

    /**
     * 모든 회원가입 요청 개수
     *
     * @return PendingMemberListResponse
     */
    public long getPendingMemberCount() {
        return pendingMemberRepository.count();
    }

    /**
     * 모든 회원가입 요청 리스트
     *
     * @return PendingMemberListResponse
     */
    public List<PendingMemberResponse> getPendingMemberList() {
        List<PendingMember> pendingMemberList = pendingMemberRepository.findAll();

        return pendingMemberList.stream()
                .map(this::getPendingMemberResponseFromPendingMember)
                .toList();

    }

    /**
     * 특정 PendingMember 상세 정보 확인
     *
     * @return PendingMemberDetailResponse
     */
    public PendingMemberDetailResponse getPendingMemberDetail(final UUID pendingMemberId) {

        PendingMember pendingMember = findPendingMember(pendingMemberId);

        PendingMemberResponse pendingMemberResponse = getPendingMemberResponseFromPendingMember(pendingMember);

        PendingGraduationResponse pendingGraduationResponse = pendingGraduationMapper.toResponse(
                pendingMember.getPendingGraduation());

        return new PendingMemberDetailResponse(pendingMemberResponse, pendingGraduationResponse);
    }

    /**
     * 회원가입 요청 단건 수락
     *
     * @param pendingMemberId 회원가입 요청 대상
     * @return Member 생성된 Member 객체
     */
    @Transactional
    public Member acceptPendingMember(final UUID pendingMemberId) {
        PendingMember pendingMember = findPendingMember(pendingMemberId);

        Graduation graduation = null;
        if (pendingMember.isGraduation()) {
            PendingGraduation pendingGraduation = pendingMember.getPendingGraduation();
            graduation = saveGraduation(pendingGraduation);
        }

        Member member = memberMapper.toEntity(pendingMember, graduation);
        Member savedMember = memberRepository.save(member);

        pendingMemberRepository.delete(pendingMember);
        return savedMember;
    }


    private Graduation saveGraduation(final PendingGraduation pendingGraduation) {
        Graduation graduation = graduationMapper.toEntity(pendingGraduation);
        return graduationRepository.save(graduation);
    }

    private PendingMember findPendingMember(final UUID pendingMemberId) {
        return pendingMemberRepository.findById(pendingMemberId)
                .orElseThrow(() -> new PendingMemberDomainException("pending member not found " + pendingMemberId,
                        HttpStatus.NOT_FOUND));
    }

    /**
     * 회원가입 요청 단건 거절 -> pendingMember 객체 삭제
     *
     * @param pendingMemberId pk
     */
    @Transactional
    public void rejectPendingMember(final UUID pendingMemberId) {
        PendingMember pendingMember = findPendingMember(pendingMemberId);
        pendingMemberRepository.delete(pendingMember);
    }
}
