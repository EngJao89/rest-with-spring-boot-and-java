package engjao89.rest_with_spring_boot_and_java.service;

import engjao89.rest_with_spring_boot_and_java.exception.ResourceNotFoundException;
import engjao89.rest_with_spring_boot_and_java.model.Person;
import engjao89.rest_with_spring_boot_and_java.repository.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonServices {

    private static final Logger logger = LoggerFactory.getLogger(PersonServices.class);

    @Autowired
    PersonRepository repository;


    public List<Person> findAll() {
        logger.debug("Iniciando busca de todas as pessoas");
        List<Person> persons = repository.findAll();
        logger.info("Total de pessoas encontradas: {}", persons.size());
        logger.debug("Lista de pessoas: {}", persons);
        return persons;
    }

    public Person findById(Long id) {
        logger.debug("Buscando pessoa com ID: {}", id);
        Person person = repository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Pessoa não encontrada com ID: {}", id);
                    return new ResourceNotFoundException("No records found for this ID!");
                });
        logger.info("Pessoa encontrada: {} {} (ID: {})", person.getFirstName(), person.getLastName(), person.getId());
        return person;
    }

    public Person create(Person person) {
        logger.debug("Iniciando criação de nova pessoa: {} {}", person.getFirstName(), person.getLastName());
        person.setId(null);
        Person savedPerson = repository.save(person);
        logger.info("Pessoa criada com sucesso! ID: {}, Nome: {} {}", 
                   savedPerson.getId(), savedPerson.getFirstName(), savedPerson.getLastName());
        logger.debug("Dados completos da pessoa criada: {}", savedPerson);
        return savedPerson;
    }

    public Person update(Person person) {
        logger.debug("Iniciando atualização da pessoa com ID: {}", person.getId());
        Person entity = repository.findById(person.getId())
                .orElseThrow(() -> {
                    logger.warn("Tentativa de atualizar pessoa inexistente com ID: {}", person.getId());
                    return new ResourceNotFoundException("No records found for this ID!");
                });
        
        logger.debug("Dados antigos - Nome: {} {}, Endereço: {}, Gênero: {}", 
                    entity.getFirstName(), entity.getLastName(), entity.getAddress(), entity.getGender());
        
        entity.setFirstName(person.getFirstName());
        entity.setLastName(person.getLastName());
        entity.setAddress(person.getAddress());
        entity.setGender(person.getGender());

        Person updatedPerson = repository.save(entity);
        logger.info("Pessoa atualizada com sucesso! ID: {}, Novo nome: {} {}", 
                   updatedPerson.getId(), updatedPerson.getFirstName(), updatedPerson.getLastName());
        logger.debug("Dados completos da pessoa atualizada: {}", updatedPerson);
        return updatedPerson;
    }

    public void delete(Long id) {
        logger.debug("Iniciando exclusão da pessoa com ID: {}", id);
        Person entity = repository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Tentativa de excluir pessoa inexistente com ID: {}", id);
                    return new ResourceNotFoundException("No records found for this ID!");
                });
        
        logger.debug("Pessoa a ser excluída: {} {} (ID: {})", 
                    entity.getFirstName(), entity.getLastName(), entity.getId());
        repository.delete(entity);
        logger.info("Pessoa excluída com sucesso! ID: {}, Nome: {} {}", 
                   id, entity.getFirstName(), entity.getLastName());
    }
}
