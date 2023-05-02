package com.udacity.jdnd.course3.critter.pet;

import com.udacity.jdnd.course3.critter.service.IPetService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Handles web requests related to Pets.
 */
@RestController
@RequestMapping("/pet")
public class PetController {

    private final IPetService iPetService;

    public PetController(IPetService iPetService) {
        this.iPetService = iPetService;
    }

    @PostMapping
    public PetDTO savePet(@RequestBody PetDTO petDTO) {
        return iPetService.save(petDTO);
    }

    @GetMapping("/{petId}")
    public PetDTO getPet(@PathVariable long petId) {
        return iPetService.findById(petId);
    }

    @GetMapping
    public List<PetDTO> getPets(){
        return iPetService.findAll();
    }

    @GetMapping("/owner/{ownerId}")
    public List<PetDTO> getPetsByOwner(@PathVariable long ownerId) {
        return iPetService.findByOwnerId(ownerId);
    }
}
