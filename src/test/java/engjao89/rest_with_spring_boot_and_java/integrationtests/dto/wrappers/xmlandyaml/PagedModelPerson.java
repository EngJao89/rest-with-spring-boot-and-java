package engjao89.rest_with_spring_boot_and_java.integrationtests.dto.wrappers.xmlandyaml;

import engjao89.rest_with_spring_boot_and_java.data.dto.V1.PersonDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;

@XmlRootElement(name = "pagedModelPerson")
public class PagedModelPerson {

    @JsonProperty("content")
    @XmlElement(name = "content")
    private List<PersonDTO> content;

    public PagedModelPerson() {}

    public List<PersonDTO> getContent() {
        return content;
    }

    public void setContent(List<PersonDTO> content) {
        this.content = content;
    }
}