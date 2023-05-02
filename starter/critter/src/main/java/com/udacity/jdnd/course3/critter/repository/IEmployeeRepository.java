package com.udacity.jdnd.course3.critter.repository;

import com.udacity.jdnd.course3.critter.entity.EmployeeEntity;
import com.udacity.jdnd.course3.critter.user.EmployeeSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Set;

@Repository
public interface IEmployeeRepository extends JpaRepository<EmployeeEntity, Long>, JpaSpecificationExecutor<EmployeeEntity> {

//    @Query("SELECT ee FROM EmployeeEntity ee WHERE ee.daysAvailable = :dayOfWeek AND ee.skills IN (:employeeSkills)")
    List<EmployeeEntity> findByDaysAvailableAndAndSkillsIn(DayOfWeek dayOfWeek, Set<EmployeeSkill> employeeSkills);

}
