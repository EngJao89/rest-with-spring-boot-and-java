package engjao89.rest_with_spring_boot_and_java.file.importer.contract;

import engjao89.rest_with_spring_boot_and_java.data.dto.V1.PersonDTO;

import java.io.InputStream;
import java.util.List;

public interface FileImporter {

    List<PersonDTO> importFile(InputStream inputStream) throws Exception;
}
