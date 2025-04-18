package org.nova.backend.member.helper;

import org.nova.backend.member.domain.model.entity.Graduation;
import org.nova.backend.member.domain.model.entity.Member;
import org.nova.backend.member.domain.model.entity.ProfilePhoto;
import org.nova.backend.member.domain.model.valueobject.Role;

import java.util.UUID;

public class MemberFixture {

    public static Member createStudent() {
        return new Member(
                UUID.randomUUID(),
                "2020069049",
                "password123!",
                "홍길동",
                "honggildong@example.com",
                false,
                4,
                2,
                false,
                new ProfilePhoto(UUID.randomUUID(), "student.jpg", "https://example.com/photos/student.jpg"),
                "010-1234-5678",
                "안녕하세요! 소프트웨어공학과 재학생입니다.",
                "2000-01-01",
                Role.GENERAL,
                null,
                false
        );
    }

    public static Member createGraduatedStudent() {
        return new Member(
                UUID.randomUUID(),
                "20181234",
                "password456!",
                "이몽룡",
                "leemongryong@example.com",
                true,
                6,
                2,
                false,
                new ProfilePhoto(UUID.randomUUID(), "graduated.jpg", "https://example.com/photos/graduated.jpg"),
                "010-5678-1234",
                "졸업 후 IT 업계에서 일하고 있습니다.",
                "1998-05-05",
                Role.GENERAL,
                new Graduation(UUID.randomUUID(), 2022, true, true, "백엔드 개발자","https://tistory.com", "블로그 사이트"),
                false

        );
    }
}
