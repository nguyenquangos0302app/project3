package com.udacity.jdnd.course3.critter.service.impl;

import com.udacity.jdnd.course3.critter.entity.CustomerEntity;
import com.udacity.jdnd.course3.critter.entity.PetEntity;
import com.udacity.jdnd.course3.critter.pet.PetDTO;
import com.udacity.jdnd.course3.critter.repository.ICustomerRepository;
import com.udacity.jdnd.course3.critter.repository.IPetRepository;
import com.udacity.jdnd.course3.critter.service.ICustomerService;
import com.udacity.jdnd.course3.critter.service.IPetService;
import com.udacity.jdnd.course3.critter.user.CustomerDTO;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements ICustomerService {

    private final ICustomerRepository iCustomerRepository;

    private final IPetRepository iPetRepository;

    private final IPetService iPetService;

    public CustomerServiceImpl(ICustomerRepository iCustomerRepository,
                               IPetRepository iPetRepository,
                               IPetService iPetService) {
        this.iCustomerRepository = iCustomerRepository;
        this.iPetRepository = iPetRepository;
        this.iPetService = iPetService;
    }

    @Override
    @Transactional
    public CustomerDTO save(CustomerDTO customerDTO) {
        ModelMapper modelMapper = new ModelMapper();
        CustomerEntity customerEntity = modelMapper.map(customerDTO, CustomerEntity.class);

        iCustomerRepository.save(customerEntity);

        return modelMapper.map(customerEntity, CustomerDTO.class);
    }

    @Override
    public CustomerDTO findById(Long id) {
        ModelMapper modelMapper = new ModelMapper();

        CustomerEntity customerEntity = iCustomerRepository.findById(id).orElseThrow(EntityNotFoundException::new);

        List<Long> petIds = new ArrayList<>();
        if (!CollectionUtils.isEmpty(customerEntity.getPets())) {
            for (PetEntity pet: customerEntity.getPets()) {
                petIds.add(pet.getId());
            }
        }

        CustomerDTO customerDTO = modelMapper.map(customerEntity, CustomerDTO.class);
        customerDTO.setPetIds(petIds);

        return customerDTO;
    }

    @Override
    public List<CustomerDTO> findAll() {
        ModelMapper modelMapper = new ModelMapper();

        List<CustomerEntity> customerEntities = iCustomerRepository.findAll();

        List<CustomerDTO> customerDTOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(customerEntities)) {
            for (CustomerEntity customer : customerEntities) {
                CustomerDTO customerDTO = modelMapper.map(customer, CustomerDTO.class);
                if (!CollectionUtils.isEmpty(customer.getPets())) {
                    List<Long> petIds = customer.getPets().stream().map(PetEntity::getId).collect(Collectors.toList());
                    customerDTO.setPetIds(petIds);
                }
                customerDTOS.add(customerDTO);
            }
        }

        return customerDTOS;

    }

    @Override
    public CustomerDTO findByPetId(Long petId) {
        ModelMapper modelMapper = new ModelMapper();

        PetEntity petEntity = iPetRepository.findById(petId).orElseThrow(EntityNotFoundException::new);

        CustomerEntity customerEntity = petEntity.getCustomer();
        CustomerDTO customerDTO = modelMapper.map(customerEntity, CustomerDTO.class);
        List<PetDTO> petDTOS = iPetService.findByOwnerId(customerEntity.getId());
        if (!CollectionUtils.isEmpty(petDTOS)) {
            List<Long> petIds = petDTOS.stream().map(PetDTO::getId).collect(Collectors.toList());
            customerDTO.setPetIds(petIds);
        }


        return customerDTO;
    }
}
