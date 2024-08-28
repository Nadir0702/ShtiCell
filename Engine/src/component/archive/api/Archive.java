package component.archive.api;

import component.sheet.api.Sheet;

import java.io.Serializable;
import java.nio.file.Path;
import java.util.List;

public interface Archive extends Serializable {
    void storeInArchive(Sheet sheet);
    Sheet retrieveFromArchive(int version);
    List<Integer> getAllVersionsChangesList();
    void saveToFile(String filePath);
}
