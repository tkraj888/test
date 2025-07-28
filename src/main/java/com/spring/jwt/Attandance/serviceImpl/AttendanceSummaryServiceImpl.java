package com.spring.jwt.Attandance.serviceImpl;

import com.spring.jwt.Attandance.Dto.AttendanceSummaryDailyDto;
import com.spring.jwt.Attandance.Dto.AttendanceSummaryDto;
import com.spring.jwt.Attandance.Dto.MonthlyAttendanceReportDto;
import com.spring.jwt.Attandance.entity.AttendanceSummary;
import com.spring.jwt.Attandance.repo.AttendanceSummaryRepository;
import com.spring.jwt.Attandance.Service.AttendanceSummaryService;
import com.spring.jwt.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AttendanceSummaryServiceImpl implements AttendanceSummaryService {

    private final AttendanceSummaryRepository repo;

    /**
     * Saves a new attendance summary and returns the updated data transfer object.
     *
     * Copies properties from the provided DTO to a new entity, persists it, and updates the DTO with any generated values before returning.
     *
     * @param dto the attendance summary data to save
     * @return the updated attendance summary DTO with persisted values
     */
    @Override
    public AttendanceSummaryDto saveSummary(AttendanceSummaryDto dto) {
        AttendanceSummary entity = new AttendanceSummary();
        BeanUtils.copyProperties(dto, entity);
        AttendanceSummary saved = repo.save(entity);
        BeanUtils.copyProperties(saved, dto);
        return dto;
    }

    /**
     * Retrieves an attendance summary by its unique ID.
     *
     * @param id the unique identifier of the attendance summary
     * @return the attendance summary data transfer object corresponding to the given ID
     * @throws ResourceNotFoundException if no attendance summary is found with the specified ID
     */
    @Override
    public AttendanceSummaryDto getSummaryById(Long id) {
        AttendanceSummary entity = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("AttendanceSummary not found"));
        AttendanceSummaryDto dto = new AttendanceSummaryDto();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }

    /**
     * Retrieves all attendance summaries as a list of data transfer objects.
     *
     * @return a list of AttendanceSummaryDto objects representing all attendance summaries
     */
    @Override
    public List<AttendanceSummaryDto> getAllSummaries() {
        return repo.findAll().stream().map(summary -> {
            AttendanceSummaryDto dto = new AttendanceSummaryDto();
            BeanUtils.copyProperties(summary, dto);
            return dto;
        }).collect(Collectors.toList());
    }

    /**
     * Updates an existing attendance summary with the provided data.
     *
     * @param id the ID of the attendance summary to update
     * @param dto the data transfer object containing updated attendance summary information
     * @return the updated attendance summary DTO
     * @throws ResourceNotFoundException if no attendance summary with the specified ID exists
     */
    @Override
    public AttendanceSummaryDto updateSummary(Long id, AttendanceSummaryDto dto) {
        AttendanceSummary entity = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("AttendanceSummary not found"));
        BeanUtils.copyProperties(dto, entity);
        entity.setId(id);
        repo.save(entity);
        return dto;
    }
    @Override
    public MonthlyAttendanceReportDto getMonthlyAttendanceReport(Long userId, String month) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");

        // Get all records for the user
        List<AttendanceSummary> allSummaries = repo.findByUserId(userId);

        // Filter by month (yyyy-MM format)
        List<AttendanceSummary> summaries = allSummaries.stream()
                .filter(summary -> summary.getDate() != null && summary.getDate().format(formatter).equals(month))
                .collect(Collectors.toList());

        // Initialize counters
        int present = 0;
        int halfDay = 0;
        int absent = 0;

        // Prepare daily summary list
        List<AttendanceSummaryDailyDto> dailyDtos = new ArrayList<>();

        for (AttendanceSummary summary : summaries) {
            AttendanceSummaryDailyDto dto = new AttendanceSummaryDailyDto();
            dto.setDate(summary.getDate());
            dto.setTotalWorkingHours(summary.getTotalWorkingHours());
            dto.setAttendanceStatus(summary.getAttendanceStatus());

            String status = summary.getAttendanceStatus();
            if (status != null) {
                switch (status.toLowerCase()) {
                    case "present":
                        present++;
                        break;
                    case "half day":
                        halfDay++;
                        break;
                    case "absent":
                        absent++;
                        break;
                    // You can handle more statuses here if needed
                }
            }

            dailyDtos.add(dto);
        }

        // Prepare final report DTO
        MonthlyAttendanceReportDto report = new MonthlyAttendanceReportDto();
        report.setUserId(userId);
        report.setMonth(month);
        report.setPresentDays(present);
        report.setHalfDays(halfDay);
        report.setAbsentDays(absent);
        report.setTotalPresentEquivalent(present + (halfDay / 2.0));
        report.setDailySummaries(dailyDtos);

        return report;
    }




}
