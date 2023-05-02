package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.user.CustomerDTO;

import java.util.List;

public interface ICustomerService {

    CustomerDTO save(CustomerDTO customerDTO);

    CustomerDTO findById(Long id);

    List<CustomerDTO> findAll();

    CustomerDTO findByPetId(Long petId);

}
