package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.user.EmployeeDTO;
import com.udacity.jdnd.course3.critter.user.EmployeeRequestDTO;
import com.udacity.jdnd.course3.critter.user.EmployeeSkill;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Set;

public interface IEmployeeService {

    EmployeeDTO save(EmployeeDTO employeeDTO);

    EmployeeDTO addScheduleById(Long id, Set<DayOfWeek> dayOfWeekList);

    List<EmployeeDTO> findByDayOfWeekAndSkills(EmployeeRequestDTO employeeDTO);

    EmployeeDTO findById(Long id);

}
