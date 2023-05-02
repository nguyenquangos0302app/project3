package com.udacity.jdnd.course3.critter.service.impl;

import com.udacity.jdnd.course3.critter.entity.EmployeeEntity;
import com.udacity.jdnd.course3.critter.repository.IEmployeeRepository;
import com.udacity.jdnd.course3.critter.service.IEmployeeService;
import com.udacity.jdnd.course3.critter.user.EmployeeDTO;
import com.udacity.jdnd.course3.critter.user.EmployeeRequestDTO;
import com.udacity.jdnd.course3.critter.user.EmployeeSkill;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.Predicate;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements IEmployeeService {

    private final IEmployeeRepository iEmployeeRepository;

    public EmployeeServiceImpl(IEmployeeRepository iEmployeeRepository) {
        this.iEmployeeRepository = iEmployeeRepository;
    }

    @Override
    @Transactional
    public EmployeeDTO save(EmployeeDTO employeeDTO) {
        ModelMapper modelMapper = new ModelMapper();

        EmployeeEntity employeeEntity = modelMapper.map(employeeDTO, EmployeeEntity.class);
        iEmployeeRepository.save(employeeEntity);

        return modelMapper.map(employeeEntity, EmployeeDTO.class);
    }

    @Override
    public EmployeeDTO addScheduleById(Long id, Set<DayOfWeek> dayOfWeekList) {
        ModelMapper modelMapper = new ModelMapper();
        EmployeeEntity employeeEntity = iEmployeeRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        employeeEntity.setDaysAvailable(dayOfWeekList);

        iEmployeeRepository.save(employeeEntity);

        return modelMapper.map(employeeEntity, EmployeeDTO.class);
    }

    @Override
    public List<EmployeeDTO> findByDayOfWeekAndSkills(EmployeeRequestDTO employeeDTO) {
        ModelMapper modelMapper = new ModelMapper();
        DayOfWeek dateOfWeek = employeeDTO.getDate().getDayOfWeek();

//        Specification<EmployeeEntity> spec = (root, query, cb) -> {
//            List<Predicate> predicates = new ArrayList<>();
//            for (EmployeeSkill value : employeeDTO.getSkills()) {
//                predicates.add(cb.equal(root.get("skills"), value));
//            }
//
//            predicates.add(cb.equal(root.get("daysAvailable"), dateOfWeek));
//            return cb.and(predicates.toArray(new Predicate[0]));
//        };
//        List<EmployeeEntity> employeeEntityList = iEmployeeRepository.findAll(spec);
        List<EmployeeEntity> employeeEntityList = iEmployeeRepository.findByDaysAvailableAndAndSkillsIn(dateOfWeek, employeeDTO.getSkills());
        List<EmployeeDTO> employeeDTOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(employeeEntityList)) {

            employeeEntityList = employeeEntityList.stream().filter(employeeEntity -> employeeEntity.getSkills().containsAll(employeeDTO.getSkills())).collect(Collectors.toList());

            for (EmployeeEntity employeeEntity : employeeEntityList) {
                EmployeeDTO employeeDTO1 = modelMapper.map(employeeEntity, EmployeeDTO.class);
                employeeDTOS.add(employeeDTO1);
            }
        }
        return employeeDTOS;
    }

    @Override
    public EmployeeDTO findById(Long id) {
        ModelMapper modelMapper = new ModelMapper();
        EmployeeEntity employeeEntity = iEmployeeRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        return modelMapper.map(employeeEntity, EmployeeDTO.class);
    }

}
