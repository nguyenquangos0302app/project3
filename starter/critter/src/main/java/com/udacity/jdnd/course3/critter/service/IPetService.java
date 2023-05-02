package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.pet.PetDTO;

import java.util.List;

public interface IPetService {

    PetDTO save(PetDTO petDTO);

    List<PetDTO> findAll();

    List<PetDTO> findByOwnerId(Long ownerId);
    
    PetDTO findById(Long id);

}
