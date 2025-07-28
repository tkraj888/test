package com.spring.jwt.Attandance.Service;

import com.spring.jwt.Attandance.Dto.AttendanceSummaryDto;
import com.spring.jwt.Attandance.Dto.MonthlyAttendanceReportDto;

import java.util.List;

public interface AttendanceSummaryService {
    /**
 * Saves a new attendance summary based on the provided data transfer object.
 *
 * @param dto the attendance summary data to be saved
 * @return the saved attendance summary
 */
AttendanceSummaryDto saveSummary(AttendanceSummaryDto dto);
    /**
 * Retrieves the attendance summary corresponding to the specified ID.
 *
 * @param id the unique identifier of the attendance summary
 * @return the attendance summary data transfer object for the given ID
 */
AttendanceSummaryDto getSummaryById(Long id);
    /**
 * Retrieves a list of all attendance summaries.
 *
 * @return a list of AttendanceSummaryDto objects representing all attendance summaries
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

    /**
 * Retrieves the monthly attendance report for a specific user and month.
 *
 * @param userId the unique identifier of the user
 * @param month the month for which the attendance report is requested, typically in "YYYY-MM" format
 * @return the monthly attendance report for the specified user and month
 */
MonthlyAttendanceReportDto getMonthlyAttendanceReport(Long userId, String month);


}
