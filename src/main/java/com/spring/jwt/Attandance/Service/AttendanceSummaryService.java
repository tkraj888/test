package com.spring.jwt.Attandance.Service;

import com.spring.jwt.Attandance.Dto.AttendanceSummaryDto;

import java.util.List;

public interface AttendanceSummaryService {
    AttendanceSummaryDto saveSummary(AttendanceSummaryDto dto);
    AttendanceSummaryDto getSummaryById(Long id);
    List<AttendanceSummaryDto> getAllSummaries();
    AttendanceSummaryDto updateSummary(Long id, AttendanceSummaryDto dto);

}
