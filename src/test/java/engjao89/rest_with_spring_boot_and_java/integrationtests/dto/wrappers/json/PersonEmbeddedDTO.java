package engjao89.rest_with_spring_boot_and_java.integrationtests.dto.wrappers.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import engjao89.rest_with_spring_boot_and_java.data.dto.V1.PersonDTO;

import java.io.Serializable;
import java.util.List;

public class PersonEmbeddedDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("people")
    private List<PersonDTO> persons;

    public PersonEmbeddedDTO() {}

    public List<PersonDTO> getPersons() {
        return persons;
    }

    public void setPersons(List<PersonDTO> persons) {
        this.persons = persons;
    }
}
