package com.spring.jwt.service.impl;

import com.spring.jwt.dto.AttendanceDTO;
import com.spring.jwt.entity.Attendance;
import com.spring.jwt.entity.Employee;
import com.spring.jwt.entity.Payslip;
import com.spring.jwt.exception.ResourceNotFoundException;
import com.spring.jwt.repository.AttendanceRepository;
import com.spring.jwt.repository.EmployeeRepository;
import com.spring.jwt.repository.PayslipRepository;
import com.spring.jwt.service.AttendanceService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AttendanceServiceImpl implements AttendanceService {

    @Autowired
    private AttendanceRepository repository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private PayslipRepository payslipRepository;
    private AttendanceDTO convertToDTO(Attendance attendance) {
        AttendanceDTO dto = new AttendanceDTO();
        BeanUtils.copyProperties(attendance, dto);
        return dto;
    }

    private Attendance convertToEntity(AttendanceDTO dto) {
        Attendance attendance = new Attendance();
        BeanUtils.copyProperties(dto, attendance);
        return attendance;
    }


    @Override
    public AttendanceDTO createAttendance(AttendanceDTO dto) {
        // Step 1: Save attendance
        Attendance saved = repository.save(convertToEntity(dto));

        // Step 2: Fetch employee
        Employee emp = employeeRepository.findByUserId(Integer.valueOf(dto.getEmployeeId()))
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));

        // Step 3: Salary calculation
        double monthlyIncentive = emp.getYearlyIncentive() / 12;
        double monthlyGross = emp.getBasicPay() + emp.getHra() + emp.getSpecialAllowances()
                + emp.getTravelAllowances() + emp.getDa() + monthlyIncentive;

        double perDay = monthlyGross / dto.getTotalDays();
        double earned = perDay * dto.getPresentDays();
        double net = earned - emp.getTotalDeductions();

        // Step 4: Save Payslip
        Payslip payslip = new Payslip();
        payslip.setUserId(emp.getUserId());
        payslip.setMonth(dto.getMonth());
        payslip.setYear(dto.getYear());

        // ✅ Copy employee info snapshot
        payslip.setName(emp.getName());
        payslip.setDesignation(emp.getDesignation());
        payslip.setDepartment(emp.getDepartment());
        payslip.setLocation(emp.getLocation());
        payslip.setDateOfJoining(emp.getDateOfJoining());
        payslip.setEmployeeId(emp.getEmployeeId());
        payslip.setBankName(emp.getBankName());
        payslip.setBankAccountNumber(emp.getBankAccountNumber());
        payslip.setPan(emp.getPan());

        // ✅ Salary components
        payslip.setBasicPay(emp.getBasicPay());
        payslip.setHra(emp.getHra());
        payslip.setSpecialAllowances(emp.getSpecialAllowances());
        payslip.setTravelAllowances(emp.getTravelAllowances());
        payslip.setDa(emp.getDa());
        payslip.setYearlyIncentive(emp.getYearlyIncentive());
        payslip.setTotalDeductions(emp.getTotalDeductions());

        // ✅ Attendance summary
        payslip.setTotalDays(dto.getTotalDays());
        payslip.setPresentDays(dto.getPresentDays());
        payslip.setAbsentDays(dto.getAbsentDays());

        // ✅ Final salary
        payslip.setTotalEarnings(earned);
        payslip.setNetSalary(net);

        payslipRepository.save(payslip);

        return convertToDTO(saved);
    }


    @Override
    public AttendanceDTO getAttendance(Long id) {
        return repository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Attendance not found with ID: " + id));
    }

    @Override
    public List<AttendanceDTO> getAll() {
        return repository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public AttendanceDTO updateAttendance(Long id, AttendanceDTO dto) {
        Attendance att = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Attendance not found with ID: " + id));

        att.setEmployeeId(dto.getEmployeeId());
        att.setMonth(dto.getMonth());
        att.setYear(dto.getYear());
        att.setPresentDays(dto.getPresentDays());
        att.setAbsentDays(dto.getAbsentDays());
        att.setTotalDays(dto.getTotalDays());

        return convertToDTO(repository.save(att));
    }

    @Override
    public void deleteAttendance(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Attendance not found with ID: " + id);
        }
        repository.deleteById(id);
    }
}
