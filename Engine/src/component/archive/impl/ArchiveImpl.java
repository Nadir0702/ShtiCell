package component.archive.impl;

import component.archive.api.Archive;
import component.sheet.api.Sheet;

import java.util.*;

public class ArchiveImpl implements Archive {
    private final Map<Integer, Sheet> storedSheets = new HashMap<>();
    private final List<Integer> changesPerVersion = new LinkedList<>();

    @Override
    public void storeInArchive(Sheet sheet) {
        this.storedSheets.put(sheet.getVersion(), sheet);
        this.changesPerVersion.add(sheet.getNumOfCellsUpdated());
    }

    @Override
    public Sheet retrieveFromArchive(int version) {
        Sheet restoredSheet = this.storedSheets.get(version);

        if (restoredSheet == null) {
            throw new IllegalArgumentException("Version " + version + " does not exist.");
        }

        return restoredSheet;
    }

    @Override
    public List<Integer> getAllVersionsChangesList() {
        return this.changesPerVersion;
    }

    public static boolean isValidVersion(String version) {
        try {
            Integer.parseInt(version);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
