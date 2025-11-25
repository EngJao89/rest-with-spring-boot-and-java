package engjao89.rest_with_spring_boot_and_java;

import engjao89.rest_with_spring_boot_and_java.model.Person;
import engjao89.rest_with_spring_boot_and_java.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

@Service
public class PersonServices {

    private Logger logger = Logger.getLogger(PersonServices.class.getName());

    @Autowired
    private PersonRepository repository;

    public List<Person> findAll() {
        logger.info("Finding all People!");
        return repository.findAll();
    }

    public Person findById(String id) {
        logger.info("Finding one Person!");
        Long personId = Long.parseLong(id);
        return repository.findById(personId)
                .orElseThrow(() -> new RuntimeException("Person not found with id: " + id));
    }

    public Person create(Person person) {
        logger.info("Creating one Person!");
        return repository.save(person);
    }

    public Person update(Person person) {
        logger.info("Updating one Person!");
        Person entity = repository.findById(person.getId())
                .orElseThrow(() -> new RuntimeException("Person not found with id: " + person.getId()));
        
        entity.setFirstName(person.getFirstName());
        entity.setLastName(person.getLastName());
        entity.setAddress(person.getAddress());
        entity.setGender(person.getGender());
        
        return repository.save(entity);
    }

    public void delete(String id) {
        logger.info("Deleting one Person!");
        Long personId = Long.parseLong(id);
        Person entity = repository.findById(personId)
                .orElseThrow(() -> new RuntimeException("Person not found with id: " + id));
        repository.delete(entity);
    }
}
