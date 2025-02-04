package org.nova.backend.member.application.service;

import java.util.List;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.nova.backend.member.adapter.repository.ExecutiveHistoryRepository;
import org.nova.backend.member.adapter.repository.MemberRepository;
import org.nova.backend.member.application.dto.request.AddExecutiveHistoryRequest;
import org.nova.backend.member.application.dto.response.ExecutiveHistoryResponse;
import org.nova.backend.member.application.mapper.ExecutiveHistoryMapper;
import org.nova.backend.member.application.mapper.MemberMapper;
import org.nova.backend.member.domain.exception.ExecutiveHistoryDomainException;
import org.nova.backend.member.domain.model.entity.ExecutiveHistory;
import org.nova.backend.member.domain.model.entity.Member;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ExecutiveHistoryService {

    private final ExecutiveHistoryRepository executiveHistoryRepository;
    private final MemberRepository memberRepository;

    private final ExecutiveHistoryMapper executiveHistoryMapper;

    private final MemberService memberService;

    /**
     * 연도 리스트 불러오기
     */
    public List<Integer> getYears() {
        List<Integer> years = executiveHistoryRepository.findAllYears();
        if (years.size() <= 1) {
            return years;
        }

        int minYear = years.stream().min(Integer::compare).orElseThrow();
        int maxYear = years.stream().max(Integer::compare).orElseThrow();

        return IntStream.rangeClosed(minYear, maxYear)
                .boxed().toList();
    }

    /**
     * 임원 추가 : 특정 연도, role, 이름 또는 Member를 받음
     */
    public ExecutiveHistoryResponse addExecutiveHistory(AddExecutiveHistoryRequest request) {

        checkValidAddExecutiveHistoryRequest(request);

        Member member = null;
        if (request.getMemberId() != null) {
            member = memberService.findByMemberId(request.getMemberId());
        }

        ExecutiveHistory executiveHistory = executiveHistoryMapper.toEntity(request, member);
        ExecutiveHistory savedExecutiveHistory = executiveHistoryRepository.save(executiveHistory);

        return executiveHistoryMapper.toResponse(savedExecutiveHistory);
    }

    private void checkValidAddExecutiveHistoryRequest(AddExecutiveHistoryRequest request) {
        if (request.getName() == null && request.getMemberId() == null) {
            throw new ExecutiveHistoryDomainException("Invalid request. Please enter name or member.",
                    HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 특정 연도의 임원들 불러오기
     */

    /**
     * 특정 role의 임원 삭제
     */

}