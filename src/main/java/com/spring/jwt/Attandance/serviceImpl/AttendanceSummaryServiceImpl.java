package com.spring.jwt.Attandance.serviceImpl;

import com.spring.jwt.Attandance.Dto.AttendanceSummaryDto;
import com.spring.jwt.Attandance.entity.AttendanceSummary;
import com.spring.jwt.Attandance.repo.AttendanceSummaryRepository;
import com.spring.jwt.Attandance.Service.AttendanceSummaryService;
import com.spring.jwt.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AttendanceSummaryServiceImpl implements AttendanceSummaryService {

    private final AttendanceSummaryRepository repo;

    /**
     * Creates and persists a new attendance summary, returning the DTO updated with persisted values.
     *
     * Copies properties from the provided DTO to a new entity, saves it, and updates the DTO with any generated or persisted values before returning.
     *
     * @param dto the attendance summary data to be saved
     * @return the attendance summary DTO updated with values from the persisted entity
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
     * Retrieves the attendance summary DTO for the specified unique ID.
     *
     * @param id the unique identifier of the attendance summary
     * @return the corresponding attendance summary DTO
     * @throws ResourceNotFoundException if no attendance summary exists with the given ID
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
     * Returns a list of all attendance summaries as data transfer objects.
     *
     * @return a list of AttendanceSummaryDto instances representing all attendance summaries
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
     * Updates the attendance summary identified by the given ID with the data from the provided DTO.
     *
     * @param id the unique identifier of the attendance summary to update
     * @param dto the DTO containing the updated attendance summary data
     * @return the DTO reflecting the updated attendance summary
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


}
