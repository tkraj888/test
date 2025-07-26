package com.spring.jwt.service.impl;


import com.spring.jwt.dto.EmployeeDTO;
import com.spring.jwt.entity.Employee;
import com.spring.jwt.exception.ResourceNotFoundException;
import com.spring.jwt.repository.EmployeeRepository;
import com.spring.jwt.service.EmployeeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepository repository;




    @Override
    public EmployeeDTO createEmployee(EmployeeDTO dto) {
        Employee saved = repository.save(convertToEntity(dto));
        return convertToDTO(saved);
    }

    @Override
    public EmployeeDTO getEmployee(Long id) {
        return repository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + id));
    }

    @Override
    public EmployeeDTO updateEmployee(Long id, EmployeeDTO dto) {
        Employee emp = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + id));

        emp.setName(dto.getName());
        emp.setDesignation(dto.getDesignation());
        emp.setDepartment(dto.getDepartment());
        emp.setLocation(dto.getLocation());
        emp.setDateOfJoining(dto.getDateOfJoining());
        emp.setEmployeeId(dto.getEmployeeId());
        emp.setBankName(dto.getBankName());
        emp.setBankAccountNumber(dto.getBankAccountNumber());
        emp.setPan(dto.getPan());
        emp.setPackageAmount(dto.getPackageAmount());

        return convertToDTO(repository.save(emp));
    }


    @Override
    public List<EmployeeDTO> getAllEmployees() {
        return repository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }


    @Override
    public void deleteEmployee(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Employee not found with ID: " + id);
        }
        repository.deleteById(id);
    }


    private EmployeeDTO convertToDTO(Employee emp) {
        EmployeeDTO dto = new EmployeeDTO();
        BeanUtils.copyProperties(emp, dto);
        return dto;
    }

    private Employee convertToEntity(EmployeeDTO dto) {
        Employee emp = new Employee();
        BeanUtils.copyProperties(dto, emp);
        return emp;
    }

}
