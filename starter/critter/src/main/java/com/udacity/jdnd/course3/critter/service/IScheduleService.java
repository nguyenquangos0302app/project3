package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.schedule.ScheduleDTO;

import java.util.List;

public interface IScheduleService {

    ScheduleDTO save(ScheduleDTO scheduleDTO);

    List<ScheduleDTO> findByPetId(Long petId);

    List<ScheduleDTO> findByCustomerId(Long customerId);

    List<ScheduleDTO> findByEmployeeId(Long employeeId);

    List<ScheduleDTO> findAll();

}
