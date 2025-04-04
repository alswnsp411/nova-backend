package org.nova.backend.member.application.dto.response;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GraduationResponse {

    private UUID graduationId;
    private String year;
    private boolean contact;
    private boolean work;
    private String job;
    private String contactInfo;
    private String contactDescription;

}
