package com.spring.jwt.Attandance.Service;

import com.spring.jwt.Attandance.Dto.AttendanceSummaryDto;

import java.util.List;

public interface AttendanceSummaryService {
    /**
 * Persists a new attendance summary using the provided data.
 *
 * @param dto the attendance summary details to save
 * @return the persisted attendance summary
 */
AttendanceSummaryDto saveSummary(AttendanceSummaryDto dto);
    /**
 * Returns the attendance summary associated with the specified unique identifier.
 *
 * @param id the unique identifier of the attendance summary to retrieve
 * @return the corresponding AttendanceSummaryDto, or null if not found
 */
AttendanceSummaryDto getSummaryById(Long id);
    /**
 * Returns a list of all attendance summaries.
 *
 * @return a list of AttendanceSummaryDto objects for all recorded attendance summaries
 */
List<AttendanceSummaryDto> getAllSummaries();
    /**
 * Updates the attendance summary with the specified ID using the provided data.
 *
 * @param id the unique identifier of the attendance summary to update
 * @param dto the updated attendance summary data
 * @return the updated attendance summary
 */
AttendanceSummaryDto updateSummary(Long id, AttendanceSummaryDto dto);

}
