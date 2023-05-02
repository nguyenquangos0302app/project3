package com.udacity.jdnd.course3.critter.repository;

import com.udacity.jdnd.course3.critter.entity.PetEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IPetRepository extends JpaRepository<PetEntity, Long> {

    @Query("SELECT pe FROM PetEntity pe WHERE pe.customer.id = :ownerId")
    List<PetEntity> findByOwnerId(@Param("ownerId") Long ownerId);

}
