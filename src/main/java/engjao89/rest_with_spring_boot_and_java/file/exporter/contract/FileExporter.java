package engjao89.rest_with_spring_boot_and_java.file.exporter.contract;

import engjao89.rest_with_spring_boot_and_java.data.dto.V1.PersonDTO;
import org.springframework.core.io.Resource;

import java.util.List;

public interface FileExporter {

    Resource exportFile(List<PersonDTO> people) throws Exception;
}
