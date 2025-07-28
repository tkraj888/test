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

    @Override
    public AttendanceSummaryDto saveSummary(AttendanceSummaryDto dto) {
        AttendanceSummary entity = new AttendanceSummary();
        BeanUtils.copyProperties(dto, entity);
        AttendanceSummary saved = repo.save(entity);
        BeanUtils.copyProperties(saved, dto);
        return dto;
    }

    @Override
    public AttendanceSummaryDto getSummaryById(Long id) {
        AttendanceSummary entity = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("AttendanceSummary not found"));
        AttendanceSummaryDto dto = new AttendanceSummaryDto();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }

    @Override
    public List<AttendanceSummaryDto> getAllSummaries() {
        return repo.findAll().stream().map(summary -> {
            AttendanceSummaryDto dto = new AttendanceSummaryDto();
            BeanUtils.copyProperties(summary, dto);
            return dto;
        }).collect(Collectors.toList());
    }

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
