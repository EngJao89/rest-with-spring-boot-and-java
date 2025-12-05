package engjao89.rest_with_spring_boot_and_java.service;

import engjao89.rest_with_spring_boot_and_java.controllers.PersonController;
import engjao89.rest_with_spring_boot_and_java.data.dto.V1.PersonDTO;
import engjao89.rest_with_spring_boot_and_java.data.dto.V2.PersonDTOV2;
import engjao89.rest_with_spring_boot_and_java.exception.ResourceNotFoundException;
import engjao89.rest_with_spring_boot_and_java.mapper.custom.PersonMapper;
import engjao89.rest_with_spring_boot_and_java.model.Person;
import engjao89.rest_with_spring_boot_and_java.repository.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static engjao89.rest_with_spring_boot_and_java.mapper.ObjectMapper.parseListObjects;
import static engjao89.rest_with_spring_boot_and_java.mapper.ObjectMapper.parseObject;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class PersonServices {

    private static final Logger logger = LoggerFactory.getLogger(PersonServices.class);

    @Autowired
    PersonRepository repository;

    @Autowired
    PersonMapper converter;


    public List<PersonDTO> findAll() {

        logger.info("Finding all People!");

        return parseListObjects(repository.findAll(), PersonDTO.class);
    }

    public PersonDTO findById(Long id) {
        logger.info("Finding one Person!");

        var entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
        return parseObject(entity, PersonDTO.class);
    }

    public PersonDTO create(PersonDTO person) {

        logger.info("Creating one Person!");
        
        if (person == null) {
            logger.error("Person DTO is null");
            throw new IllegalArgumentException("Person data cannot be null");
        }
        
        var entity = parseObject(person, Person.class);
        
        // Garante que o ID seja null para forçar um INSERT
        entity.setId(null);
        
        Person savedEntity = repository.save(entity);
        logger.info("Person created successfully! ID: {}", savedEntity.getId());
        return parseObject(savedEntity, PersonDTO.class);
    }

    public PersonDTOV2 createV2(PersonDTOV2 person) {

        logger.info("Creating one Person V2!");
        var entity = converter.convertDTOtoEntity(person);

        return converter.convertEntityToDTO(repository.save(entity));
    }

    public PersonDTO update(PersonDTO person) {

        logger.info("Updating one Person! ID: {}", person != null ? person.getId() : "null");
        
        if (person == null) {
            logger.error("Person DTO is null");
            throw new IllegalArgumentException("Person data cannot be null");
        }
        
        if (person.getId() == null) {
            logger.warn("Attempt to update person without ID. Person data: {}", person);
            throw new IllegalArgumentException("Person ID is required for update operation");
        }
        
        Person entity = repository.findById(person.getId())
                .orElseThrow(() -> {
                    logger.warn("Person not found with ID: {}", person.getId());
                    return new ResourceNotFoundException("No records found for this ID!");
                });

        logger.debug("Updating person entity with ID: {}", entity.getId());
        entity.setFirstName(person.getFirstName());
        entity.setLastName(person.getLastName());
        entity.setAddress(person.getAddress());
        entity.setGender(person.getGender());
        entity.setBirthDay(person.getBirthDay());

        Person savedEntity = repository.save(entity);
        logger.info("Person updated successfully! ID: {}", savedEntity.getId());
        return parseObject(savedEntity, PersonDTO.class);
    }

    public void delete(Long id) {

        logger.info("Deleting one Person!");

        Person entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
        repository.delete(entity);
    }

    private void addHateoasLinks(PersonDTO dto) {
        dto.add(linkTo(methodOn(PersonController.class).findById(dto.getId())).withSelfRel().withType("GET"));
        dto.add(linkTo(methodOn(PersonController.class).findAll()).withRel("findAll").withType("GET"));
        dto.add(linkTo(methodOn(PersonController.class).create(dto)).withRel("create").withType("POST"));
        dto.add(linkTo(methodOn(PersonController.class).update(dto)).withRel("update").withType("PUT"));
        dto.add(linkTo(methodOn(PersonController.class).delete(dto.getId())).withRel("delete").withType("DELETE"));
    }
}
