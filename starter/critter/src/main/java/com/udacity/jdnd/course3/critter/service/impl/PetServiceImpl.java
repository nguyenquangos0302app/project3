package com.udacity.jdnd.course3.critter.service.impl;

import com.fasterxml.jackson.databind.util.BeanUtil;
import com.udacity.jdnd.course3.critter.entity.CustomerEntity;
import com.udacity.jdnd.course3.critter.entity.PetEntity;
import com.udacity.jdnd.course3.critter.pet.PetDTO;
import com.udacity.jdnd.course3.critter.repository.ICustomerRepository;
import com.udacity.jdnd.course3.critter.repository.IPetRepository;
import com.udacity.jdnd.course3.critter.service.IPetService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class PetServiceImpl implements IPetService {

    private final IPetRepository iPetRepository;

    private final ICustomerRepository iCustomerRepository;

    public PetServiceImpl(IPetRepository iPetRepository, ICustomerRepository iCustomerRepository) {
        this.iPetRepository = iPetRepository;
        this.iCustomerRepository = iCustomerRepository;
    }

    @Override
    public PetDTO save(PetDTO petDTO) {
        ModelMapper modelMapper = new ModelMapper();
        PetEntity petEntity = modelMapper.map(petDTO, PetEntity.class);

        CustomerEntity customerEntity = iCustomerRepository.findById(petDTO.getOwnerId()).orElseThrow(EntityNotFoundException::new);
        petEntity.setCustomer(customerEntity);
        customerEntity.addPet(petEntity);
        iPetRepository.save(petEntity);

        PetDTO petDTO1 = modelMapper.map(petEntity, PetDTO.class);
        petDTO1.setOwnerId(customerEntity.getId());

        return petDTO1;
    }

    @Override
    public List<PetDTO> findAll() {
        ModelMapper modelMapper = new ModelMapper();

        List<PetEntity> petEntities = iPetRepository.findAll();

        List<PetDTO> petDTOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(petEntities)) {
            for (PetEntity pet : petEntities) {
                PetDTO petDTO = modelMapper.map(pet, PetDTO.class);
                if (Objects.nonNull(pet.getCustomer())) {
                    petDTO.setOwnerId(pet.getCustomer().getId());
                }
                petDTOS.add(petDTO);
            }
        }

        return petDTOS;
    }

    @Override
    public List<PetDTO> findByOwnerId(Long ownerId) {
        ModelMapper modelMapper = new ModelMapper();

        List<PetEntity> petEntities = iPetRepository.findByOwnerId(ownerId);

        List<PetDTO> petDTOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(petEntities)) {
            for (PetEntity pet : petEntities) {
                PetDTO petDTO = modelMapper.map(pet, PetDTO.class);
                if (Objects.nonNull(pet.getCustomer())) {
                    petDTO.setOwnerId(pet.getCustomer().getId());
                }
                petDTOS.add(petDTO);
            }
        }

        return petDTOS;
    }

    @Override
    public PetDTO findById(Long id) {
        ModelMapper modelMapper = new ModelMapper();

        PetEntity petEntity = iPetRepository.findById(id).orElseThrow(EntityNotFoundException::new);

        PetDTO petDTO = modelMapper.map(petEntity, PetDTO.class);
        if (Objects.nonNull(petEntity.getCustomer())) {
            petDTO.setOwnerId(petEntity.getCustomer().getId());
        }

        return petDTO;
    }
}
