package com.spring.jwt.Attandance.Service;

import com.spring.jwt.Attandance.Dto.AttendanceSummaryDto;

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
 * Updates an existing attendance summary identified by the given ID with the provided data.
 *
 * @param id  the unique identifier of the attendance summary to update
 * @param dto the data transfer object containing updated attendance summary information
 * @return the updated attendance summary as a data transfer object
 */
AttendanceSummaryDto updateSummary(Long id, AttendanceSummaryDto dto);

}
