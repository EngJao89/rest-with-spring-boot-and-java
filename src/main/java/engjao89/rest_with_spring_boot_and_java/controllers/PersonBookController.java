package engjao89.rest_with_spring_boot_and_java.controllers;

import engjao89.rest_with_spring_boot_and_java.controllers.docs.PersonBookControllerDocs;
import engjao89.rest_with_spring_boot_and_java.data.dto.BookDTO;
import engjao89.rest_with_spring_boot_and_java.service.PersonBookServices;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/person/v1/me/books")
@Tag(name = "Person Books", description = "Endpoints for Managing the authenticated user's person book list")
public class PersonBookController implements PersonBookControllerDocs {

    @Autowired
    private PersonBookServices service;

    @GetMapping(produces = {
            MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE,
            MediaType.APPLICATION_YAML_VALUE})
    @Override
    public List<BookDTO> findMyBooks() {
        return service.findMyBooks();
    }

    @PostMapping(value = "/{bookId}",
            produces = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE,
                    MediaType.APPLICATION_YAML_VALUE})
    @Override
    public BookDTO addBookToMyList(@PathVariable("bookId") Long bookId) {
        return service.addBookToMyList(bookId);
    }

    @PutMapping(
            consumes = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE,
                    MediaType.APPLICATION_YAML_VALUE},
            produces = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE,
                    MediaType.APPLICATION_YAML_VALUE})
    @Override
    public List<BookDTO> replaceMyBooks(@RequestBody List<Long> bookIds) {
        return service.replaceMyBooks(bookIds);
    }

    @DeleteMapping(value = "/{bookId}")
    @Override
    public ResponseEntity<?> removeBookFromMyList(@PathVariable("bookId") Long bookId) {
        service.removeBookFromMyList(bookId);
        return ResponseEntity.noContent().build();
    }
}
