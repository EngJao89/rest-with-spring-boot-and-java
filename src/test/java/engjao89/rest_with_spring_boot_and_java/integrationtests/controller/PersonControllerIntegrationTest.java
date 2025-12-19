package engjao89.rest_with_spring_boot_and_java.integrationtests.controller;

import engjao89.rest_with_spring_boot_and_java.config.TestConfigs;
import engjao89.rest_with_spring_boot_and_java.data.dto.V1.PersonDTO;
import engjao89.rest_with_spring_boot_and_java.integrationtests.testcontainers.AbstractIntegrationTest;
import io.rest-assured.RestAssured;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static io.rest-assured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PersonControllerIntegrationTest extends AbstractIntegrationTest {

    private static PersonDTO person;

    @BeforeAll
    public static void setup() {
        RestAssured.basePath = "/person/v1";
    }

    @Test
    @Order(1)
    @DisplayName("JUnit test for Given Person Object When Create Then Return Saved Object")
    void testCreate() {
        person = new PersonDTO();
        person.setFirstName("João");
        person.setLastName("Ricardo");
        person.setAddress("São Paulo - Brasil");
        person.setGender("Male");

        PersonDTO createdPerson = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(person)
                .when()
                .post()
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .body()
                .as(PersonDTO.class);

        assertNotNull(createdPerson);
        assertNotNull(createdPerson.getId());
        assertNotNull(createdPerson.getFirstName());
        assertNotNull(createdPerson.getLastName());
        assertNotNull(createdPerson.getAddress());
        assertNotNull(createdPerson.getGender());

        assertTrue(createdPerson.getId() > 0);

        assertEquals("João", createdPerson.getFirstName());
        assertEquals("Ricardo", createdPerson.getLastName());
        assertEquals("São Paulo - Brasil", createdPerson.getAddress());
        assertEquals("Male", createdPerson.getGender());

        person = createdPerson;
    }

    @Test
    @Order(2)
    @DisplayName("JUnit test for Given Person Object When FindById Then Return Person Object")
    void testFindById() {
        PersonDTO foundPerson = given()
                .pathParam("id", person.getId())
                .when()
                .get("/{id}")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .body()
                .as(PersonDTO.class);

        assertNotNull(foundPerson);
        assertNotNull(foundPerson.getId());
        assertNotNull(foundPerson.getFirstName());
        assertNotNull(foundPerson.getLastName());
        assertNotNull(foundPerson.getAddress());
        assertNotNull(foundPerson.getGender());

        assertEquals(person.getId(), foundPerson.getId());
        assertEquals("João", foundPerson.getFirstName());
        assertEquals("Ricardo", foundPerson.getLastName());
        assertEquals("São Paulo - Brasil", foundPerson.getAddress());
        assertEquals("Male", foundPerson.getGender());
    }

    @Test
    @Order(3)
    @DisplayName("JUnit test for Given Person Object When Update Then Return Updated Object")
    void testUpdate() {
        person.setFirstName("João Updated");
        person.setLastName("Ricardo Updated");

        PersonDTO updatedPerson = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(person)
                .when()
                .put()
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .body()
                .as(PersonDTO.class);

        assertNotNull(updatedPerson);
        assertNotNull(updatedPerson.getId());
        assertNotNull(updatedPerson.getFirstName());
        assertNotNull(updatedPerson.getLastName());
        assertNotNull(updatedPerson.getAddress());
        assertNotNull(updatedPerson.getGender());

        assertEquals(person.getId(), updatedPerson.getId());
        assertEquals("João Updated", updatedPerson.getFirstName());
        assertEquals("Ricardo Updated", updatedPerson.getLastName());
        assertEquals("São Paulo - Brasil", updatedPerson.getAddress());
        assertEquals("Male", updatedPerson.getGender());

        person = updatedPerson;
    }

    @Test
    @Order(4)
    @DisplayName("JUnit test for Given Person Object When FindAll Then Return Person List")
    void testFindAll() {
        PersonDTO[] people = given()
                .when()
                .get()
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .body()
                .as(PersonDTO[].class);

        assertNotNull(people);
        assertTrue(people.length > 0);

        PersonDTO foundPerson = people[0];
        assertNotNull(foundPerson.getId());
        assertNotNull(foundPerson.getFirstName());
        assertNotNull(foundPerson.getLastName());
        assertNotNull(foundPerson.getAddress());
        assertNotNull(foundPerson.getGender());
    }

    @Test
    @Order(5)
    @DisplayName("JUnit test for Given Person ID When Delete Then Return No Content")
    void testDelete() {
        given()
                .pathParam("id", person.getId())
                .when()
                .delete("/{id}")
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    @Order(6)
    @DisplayName("JUnit test for Given Invalid Person ID When FindById Then Return Not Found")
    void testFindByIdNotFound() {
        given()
                .pathParam("id", 999L)
                .when()
                .get("/{id}")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }
}
