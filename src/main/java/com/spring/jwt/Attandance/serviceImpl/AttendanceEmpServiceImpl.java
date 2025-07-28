package com.spring.jwt.Attandance.serviceImpl;

import com.spring.jwt.Attandance.Dto.AttendanceEmpCreateDto;
import com.spring.jwt.Attandance.Dto.AttendanceEmpDto;
import com.spring.jwt.Attandance.ResourceAlreadyExistsException;
import com.spring.jwt.Attandance.Service.AttendanceEmpService;
import com.spring.jwt.Attandance.entity.AttendanceEmp;
import com.spring.jwt.Attandance.entity.AttendanceSummary;
import com.spring.jwt.Attandance.repo.AttendanceEmpRepository;
import com.spring.jwt.Attandance.repo.AttendanceSummaryRepository;
import com.spring.jwt.exception.ResourceNotFoundException;
import com.spring.jwt.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AttendanceEmpServiceImpl implements AttendanceEmpService {

    private final AttendanceEmpRepository repo;
    private final UserRepository userRepository;
    private final AttendanceSummaryRepository summaryRepo;

    @Override
    @Transactional
    public AttendanceEmpCreateDto saveAttendance(AttendanceEmpCreateDto dto, String type) {
        Long userId = dto.getUserId();
        LocalDate date = LocalDate.now();
        LocalTime currentTime = LocalTime.now();

        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User with ID %d does not exist.".formatted(userId));
        }

        AttendanceEmp entity = repo.findByUserIdAndDate(userId, date).orElse(null);

        switch (type.toLowerCase()) {
            case "login" -> {
                if (entity != null && entity.getLoginTime() != null) {
                    throw new ResourceAlreadyExistsException("Login already marked for today.");
                }

                if (entity == null) {
                    entity = new AttendanceEmp();
                    entity.setUserId(userId);
                    entity.setDate(date);
                }

                entity.setLoginTime(currentTime);
                entity.setWorkMode(dto.getWorkMode());
            }

            case "logout" -> {
                if (entity == null || entity.getLoginTime() == null) {
                    throw new ResourceNotFoundException("Login not marked yet for today. Cannot logout.");
                }
                if (entity.getLogoutTime() != null) {
                    throw new ResourceAlreadyExistsException("Logout already marked for today.");
                }

                entity.setLogoutTime(currentTime);
                double workingHours = Duration.between(entity.getLoginTime(), currentTime).toMinutes() / 60.0;
                entity.setWorkingHours(workingHours);

                AttendanceSummary summary = summaryRepo.findByUserIdAndDate(userId, date)
                        .orElse(new AttendanceSummary());

                summary.setUserId(userId);
                summary.setDate(date);
                summary.setTotalWorkingHours(workingHours);
                summary.setWorkMode(entity.getWorkMode());

                summary.setAttendanceStatus(
                        workingHours < 4.0 ? "Absent" :
                                workingHours < 8.5 ? "Half-Day" : "Present"
                );

                summaryRepo.save(summary);
            }

            case "leave" -> {
                if (summaryRepo.findByUserIdAndDate(userId, date).isPresent()) {
                    throw new ResourceAlreadyExistsException("Attendance already marked for today.");
                }

                AttendanceSummary summary = new AttendanceSummary();
                summary.setUserId(userId);
                summary.setDate(date);
                summary.setTotalWorkingHours(0.0);
                summary.setAttendanceStatus("Leave");

                summaryRepo.save(summary);
            }

            default -> throw new IllegalArgumentException("Invalid type: must be 'login', 'logout', or 'leave'.");
        }

        AttendanceEmp saved = repo.save(entity);

        AttendanceEmpCreateDto response = new AttendanceEmpCreateDto();
        response.setUserId(saved.getUserId());
        response.setWorkMode(saved.getWorkMode());
        return response;
    }

    @Override
    public AttendanceEmpDto getAttendanceById(Long id) {
        AttendanceEmp entity = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("AttendanceEmp not found with ID: " + id));
        AttendanceEmpDto dto = new AttendanceEmpDto();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }

    @Override
    public List<AttendanceEmpDto> getAllAttendance() {
        return repo.findAll().stream().map(emp -> {
            AttendanceEmpDto dto = new AttendanceEmpDto();
            BeanUtils.copyProperties(emp, dto);
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public AttendanceEmpDto updateAttendance(Long id, AttendanceEmpDto dto) {
        AttendanceEmp entity = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("AttendanceEmp not found with ID: " + id));

        if (dto.getLoginTime() != null) entity.setLoginTime(dto.getLoginTime());
        if (dto.getLogoutTime() != null) entity.setLogoutTime(dto.getLogoutTime());
        if (dto.getDate() != null) entity.setDate(dto.getDate());
        if (dto.getWorkMode() != null) entity.setWorkMode(dto.getWorkMode());

        if (entity.getLoginTime() != null && entity.getLogoutTime() != null) {
            double workingHours = Duration.between(entity.getLoginTime(), entity.getLogoutTime()).toMinutes() / 60.0;
            entity.setWorkingHours(workingHours);

            AttendanceSummary summary = summaryRepo.findByUserIdAndDate(entity.getUserId(), entity.getDate())
                    .orElse(new AttendanceSummary());

            summary.setUserId(entity.getUserId());
            summary.setDate(entity.getDate());
            summary.setTotalWorkingHours(workingHours);
            summary.setWorkMode(entity.getWorkMode());

            summary.setAttendanceStatus(
                    workingHours < 4.0 ? "Absent" :
                            workingHours < 8.5 ? "Half-Day" : "Present"
            );

            summaryRepo.save(summary);
        }

        AttendanceEmp saved = repo.save(entity);
        AttendanceEmpDto response = new AttendanceEmpDto();
        BeanUtils.copyProperties(saved, response);
        return response;
    }

    @Override
    public List<AttendanceEmpDto> getAttendanceByUserIdAndDateRange(Long userId, LocalDate startDate, LocalDate endDate) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User with ID %d not found.".formatted(userId));
        }

        if (startDate == null || endDate == null || startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Invalid date range provided.");
        }

        List<AttendanceEmp> list = repo.findByUserIdAndDateBetween(userId, startDate, endDate);
        if (list.isEmpty()) {
            throw new ResourceNotFoundException("No attendance found for user ID %d in the given date range.".formatted(userId));
        }

        return list.stream().map(emp -> {
            AttendanceEmpDto dto = new AttendanceEmpDto();
            BeanUtils.copyProperties(emp, dto);
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public List<AttendanceEmpDto> getAttendanceByUserIdAndMonth(Long userId, int month, int year) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User with ID %d not found.".formatted(userId));
        }

        if (month < 1 || month > 12) {
            throw new IllegalArgumentException("Invalid month: " + month);
        }

        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = start.withDayOfMonth(start.lengthOfMonth());

        List<AttendanceEmp> list = repo.findByUserIdAndDateBetween(userId, start, end);
        if (list.isEmpty()) {
            throw new ResourceNotFoundException("No attendance found for user ID %d in %d/%d.".formatted(userId, month, year));
        }

        return list.stream().map(emp -> {
            AttendanceEmpDto dto = new AttendanceEmpDto();
            BeanUtils.copyProperties(emp, dto);
            return dto;
        }).collect(Collectors.toList());
    }
}
