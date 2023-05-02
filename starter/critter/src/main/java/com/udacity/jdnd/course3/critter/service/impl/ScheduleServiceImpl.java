package com.udacity.jdnd.course3.critter.service.impl;

import com.udacity.jdnd.course3.critter.entity.CustomerEntity;
import com.udacity.jdnd.course3.critter.entity.EmployeeEntity;
import com.udacity.jdnd.course3.critter.entity.PetEntity;
import com.udacity.jdnd.course3.critter.entity.ScheduleEntity;
import com.udacity.jdnd.course3.critter.repository.ICustomerRepository;
import com.udacity.jdnd.course3.critter.repository.IEmployeeRepository;
import com.udacity.jdnd.course3.critter.repository.IPetRepository;
import com.udacity.jdnd.course3.critter.repository.IScheduleRepository;
import com.udacity.jdnd.course3.critter.schedule.ScheduleDTO;
import com.udacity.jdnd.course3.critter.service.IScheduleService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ScheduleServiceImpl implements IScheduleService {

    private final IScheduleRepository iScheduleRepository;

    private final IEmployeeRepository iEmployeeRepository;

    private final IPetRepository iPetRepository;

    private final ICustomerRepository iCustomerRepository;

    public ScheduleServiceImpl(IScheduleRepository iScheduleRepository,
                               IEmployeeRepository iEmployeeRepository,
                               IPetRepository iPetRepository,
                               ICustomerRepository iCustomerRepository) {
        this.iScheduleRepository = iScheduleRepository;
        this.iEmployeeRepository = iEmployeeRepository;
        this.iPetRepository = iPetRepository;
        this.iCustomerRepository = iCustomerRepository;
    }

    @Override
    @Transactional
    public ScheduleDTO save(ScheduleDTO scheduleDTO) {
        ModelMapper modelMapper = new ModelMapper();

        List<EmployeeEntity> employeeEntityList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(scheduleDTO.getEmployeeIds())) {
            for (Long employeeId : scheduleDTO.getEmployeeIds()) {
                EmployeeEntity employeeEntity = iEmployeeRepository.findById(employeeId).orElseThrow(EntityNotFoundException::new);
                employeeEntityList.add(employeeEntity);
            }
        }

        List<PetEntity> petEntities = new ArrayList<>();
        if (!CollectionUtils.isEmpty(scheduleDTO.getPetIds())) {
            for (Long petId : scheduleDTO.getPetIds()) {
                PetEntity petEntity = iPetRepository.findById(petId).orElseThrow(EntityNotFoundException::new);
                petEntities.add(petEntity);
            }
        }

        ScheduleEntity schedule = modelMapper.map(scheduleDTO, ScheduleEntity.class);
        schedule.setEmployees(employeeEntityList);
        schedule.setPets(petEntities);

        iScheduleRepository.save(schedule);
        scheduleDTO.setId(schedule.getId());

        return scheduleDTO;
    }

    @Override
    public List<ScheduleDTO> findByPetId(Long petId) {
        ModelMapper modelMapper = new ModelMapper();
        List<ScheduleEntity> scheduleEntities = iScheduleRepository.findByPets_Id(petId);
        List<ScheduleDTO> scheduleDTOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(scheduleEntities)) {
            for (ScheduleEntity schedule : scheduleEntities) {
                ScheduleDTO scheduleDTO = modelMapper.map(schedule, ScheduleDTO.class);

                if (!CollectionUtils.isEmpty(schedule.getEmployees())) {
                    List<Long> employeeIds = schedule.getEmployees().stream().map(EmployeeEntity::getId).collect(Collectors.toList());
                    scheduleDTO.setEmployeeIds(employeeIds);
                }

                if (!CollectionUtils.isEmpty(schedule.getPets())) {
                    List<Long> petIds = schedule.getPets().stream().map(PetEntity::getId).collect(Collectors.toList());
                    scheduleDTO.setPetIds(petIds);
                }

                scheduleDTOS.add(scheduleDTO);
            }
        }

        return scheduleDTOS;
    }

    @Override
    public List<ScheduleDTO> findByCustomerId(Long customerId) {
        CustomerEntity customerEntity = iCustomerRepository.findById(customerId).orElseThrow(EntityNotFoundException::new);

        List<ScheduleDTO> scheduleDTOS = new ArrayList<>();

        if (!CollectionUtils.isEmpty(customerEntity.getPets())) {
            for (PetEntity pet : customerEntity.getPets()) {
                scheduleDTOS.addAll(findByPetId(pet.getId()));
            }
        }

        return scheduleDTOS;
    }

    @Override
    public List<ScheduleDTO> findByEmployeeId(Long employeeId) {
        ModelMapper modelMapper = new ModelMapper();
        List<ScheduleEntity> scheduleEntities = iScheduleRepository.findByEmployees_Id(employeeId);
        List<ScheduleDTO> scheduleDTOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(scheduleEntities)) {
            for (ScheduleEntity schedule : scheduleEntities) {
                ScheduleDTO scheduleDTO = modelMapper.map(schedule, ScheduleDTO.class);

                if (!CollectionUtils.isEmpty(schedule.getEmployees())) {
                    List<Long> employeeIds = schedule.getEmployees().stream().map(EmployeeEntity::getId).collect(Collectors.toList());
                    scheduleDTO.setEmployeeIds(employeeIds);
                }

                if (!CollectionUtils.isEmpty(schedule.getPets())) {
                    List<Long> petIds = schedule.getPets().stream().map(PetEntity::getId).collect(Collectors.toList());
                    scheduleDTO.setPetIds(petIds);
                }

                scheduleDTOS.add(scheduleDTO);
            }
        }

        return scheduleDTOS;
    }

    @Override
    public List<ScheduleDTO> findAll() {
        ModelMapper modelMapper = new ModelMapper();
        List<ScheduleEntity> scheduleEntities = iScheduleRepository.findAll();
        List<ScheduleDTO> scheduleDTOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(scheduleEntities)) {
            for (ScheduleEntity schedule : scheduleEntities) {
                ScheduleDTO scheduleDTO = modelMapper.map(schedule, ScheduleDTO.class);

                if (!CollectionUtils.isEmpty(schedule.getEmployees())) {
                    List<Long> employeeIds = schedule.getEmployees().stream().map(EmployeeEntity::getId).collect(Collectors.toList());
                    scheduleDTO.setEmployeeIds(employeeIds);
                }

                if (!CollectionUtils.isEmpty(schedule.getPets())) {
                    List<Long> petIds = schedule.getPets().stream().map(PetEntity::getId).collect(Collectors.toList());
                    scheduleDTO.setPetIds(petIds);
                }

                scheduleDTOS.add(scheduleDTO);
            }
        }

        return scheduleDTOS;
    }
}
