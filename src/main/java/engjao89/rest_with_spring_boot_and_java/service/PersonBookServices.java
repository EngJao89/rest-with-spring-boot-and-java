package engjao89.rest_with_spring_boot_and_java.service;

import engjao89.rest_with_spring_boot_and_java.controllers.BookController;
import engjao89.rest_with_spring_boot_and_java.controllers.PersonBookController;
import engjao89.rest_with_spring_boot_and_java.data.dto.BookDTO;
import engjao89.rest_with_spring_boot_and_java.exception.BadRequestException;
import engjao89.rest_with_spring_boot_and_java.exception.ResourceNotFoundException;
import engjao89.rest_with_spring_boot_and_java.model.Book;
import engjao89.rest_with_spring_boot_and_java.model.Person;
import engjao89.rest_with_spring_boot_and_java.model.User;
import engjao89.rest_with_spring_boot_and_java.repository.BookRepository;
import engjao89.rest_with_spring_boot_and_java.repository.PersonRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static engjao89.rest_with_spring_boot_and_java.mapper.ObjectMapper.parseListObjects;
import static engjao89.rest_with_spring_boot_and_java.mapper.ObjectMapper.parseObject;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class PersonBookServices {

    private final Logger logger = LoggerFactory.getLogger(PersonBookServices.class.getName());

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private BookRepository bookRepository;

    public List<BookDTO> findMyBooks() {
        logger.info("Finding books for authenticated user's person!");
        Person person = getOrCreateAuthenticatedPerson();
        List<Book> books = person.getBooks() == null ? List.of() : person.getBooks();
        List<BookDTO> dtos = parseListObjects(books, BookDTO.class);
        dtos.forEach(this::addHateoasLinks);
        return dtos;
    }

    @Transactional
    public BookDTO addBookToMyList(Long bookId) {
        logger.info("Adding book {} to authenticated user's person list!", bookId);
        Person person = getOrCreateAuthenticatedPerson();
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this Book ID!"));

        if (person.getBooks() == null) {
            person.setBooks(new ArrayList<>());
        }

        boolean alreadyInList = person.getBooks().stream()
                .anyMatch(existing -> Objects.equals(existing.getId(), bookId));
        if (alreadyInList) {
            throw new BadRequestException("Book is already in the person's list!");
        }

        person.getBooks().add(book);
        personRepository.save(person);

        BookDTO dto = parseObject(book, BookDTO.class);
        addHateoasLinks(dto);
        return dto;
    }

    @Transactional
    public List<BookDTO> replaceMyBooks(List<Long> bookIds) {
        logger.info("Replacing books for authenticated user's person list!");
        if (bookIds == null) {
            throw new BadRequestException("Book IDs list cannot be null!");
        }

        Person person = getOrCreateAuthenticatedPerson();
        List<Book> books = new ArrayList<>();
        for (Long bookId : bookIds) {
            Book book = bookRepository.findById(bookId)
                    .orElseThrow(() -> new ResourceNotFoundException("No records found for Book ID: " + bookId));
            books.add(book);
        }

        person.setBooks(books);
        personRepository.save(person);

        List<BookDTO> dtos = parseListObjects(books, BookDTO.class);
        dtos.forEach(this::addHateoasLinks);
        return dtos;
    }

    @Transactional
    public void removeBookFromMyList(Long bookId) {
        logger.info("Removing book {} from authenticated user's person list!", bookId);
        Person person = getOrCreateAuthenticatedPerson();
        if (person.getBooks() == null || person.getBooks().isEmpty()) {
            throw new ResourceNotFoundException("Book is not in the person's list!");
        }

        boolean removed = person.getBooks().removeIf(book -> Objects.equals(book.getId(), bookId));
        if (!removed) {
            throw new ResourceNotFoundException("Book is not in the person's list!");
        }
        personRepository.save(person);
    }

    @Transactional
    public Person getOrCreateAuthenticatedPerson() {
        User user = getAuthenticatedUser();
        Person person = personRepository.findByUserId(user.getId());
        if (person != null) {
            return person;
        }

        logger.info("Creating person profile for user {}!", user.getUsername());
        person = new Person();
        person.setUserId(user.getId());
        applyNameFromUser(person, user);
        person.setAddress("N/A");
        person.setGender("N/A");
        person.setEnabled(true);
        person.setBooks(new ArrayList<>());
        return personRepository.save(person);
    }

    public void createPersonForUser(User user) {
        if (user == null || user.getId() == null) {
            return;
        }
        if (personRepository.findByUserId(user.getId()) != null) {
            return;
        }

        Person person = new Person();
        person.setUserId(user.getId());
        applyNameFromUser(person, user);
        person.setAddress("N/A");
        person.setGender("N/A");
        person.setEnabled(true);
        person.setBooks(new ArrayList<>());
        personRepository.save(person);
    }

    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof User user)) {
            throw new BadRequestException("Authenticated user not found!");
        }
        return user;
    }

    private void applyNameFromUser(Person person, User user) {
        String fullName = user.getFullName() == null ? user.getUsername() : user.getFullName().trim();
        String[] parts = fullName.split("\\s+", 2);
        person.setFirstName(parts[0]);
        person.setLastName(parts.length > 1 ? parts[1] : parts[0]);
    }

    private void addHateoasLinks(BookDTO dto) {
        dto.add(linkTo(methodOn(BookController.class).findById(dto.getId())).withSelfRel().withType("GET"));
        dto.add(linkTo(methodOn(PersonBookController.class).findMyBooks()).withRel("myBooks").withType("GET"));
        dto.add(linkTo(methodOn(PersonBookController.class).addBookToMyList(dto.getId())).withRel("addToMyList").withType("POST"));
        dto.add(linkTo(methodOn(PersonBookController.class).removeBookFromMyList(dto.getId())).withRel("removeFromMyList").withType("DELETE"));
    }
}
