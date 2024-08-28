package logic;

import component.archive.api.Archive;
import component.archive.impl.ArchiveImpl;
import component.cell.api.Cell;
import component.cell.impl.CellImpl;
import component.sheet.api.Sheet;
import component.sheet.impl.SheetImpl;
import dto.CellDTO;
import dto.SheetDTO;
import dto.VersionChangesDTO;
import jakarta.xml.bind.JAXBException;
import jaxb.converter.api.XMLToSheetConverter;
import jaxb.converter.impl.XMLToSheetConverterImpl;

import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class EngineImpl implements Engine{
    private Sheet sheet = null;
    private Archive archive = null;

    @Override
    public void LoadData(String path) {
        try {
            XMLToSheetConverter converter = new XMLToSheetConverterImpl();
            this.sheet = converter.convert(path);
            this.archive = new ArchiveImpl();
            this.archive.storeInArchive(this.sheet.copySheet());
        } catch (JAXBException | FileNotFoundException e ) {
            throw new RuntimeException("Error loading data from file", e);
        }
    }

    @Override
    public SheetDTO getSheetAsDTO() {
        return new SheetDTO(this.sheet);
    }

    @Override
    public CellDTO getSingleCellData(String cellID) {
        return new CellDTO(this.sheet.getCell(cellID));
    }

    @Override
    public void updateSingleCellData(String cellID, String value) {
        SheetImpl newSheetVersion = this.sheet.copySheet();
        updateCell(cellID, value, newSheetVersion);
        this.sheet = this.sheet.updateSheet(newSheetVersion);
        this.archive.storeInArchive(this.sheet.copySheet());
    }

    private void updateCell(String cellID, String value, Sheet newSheetVersion) {
        Cell cellToUpdate = newSheetVersion.getCell(cellID);
        if (cellToUpdate != null) {
            cellToUpdate.setOriginalValue(value, newSheetVersion.getVersion() + 1);
        } else {
            cellToUpdate = new CellImpl(cellID, value, newSheetVersion.getVersion(), newSheetVersion);
            newSheetVersion.getCells().put(cellToUpdate.getCellId(), cellToUpdate);
        }
    }

    @Override
    public VersionChangesDTO showVersions() {
        return new VersionChangesDTO(this.archive.getAllVersionsChangesList());
    }

    @Override
    public SheetDTO getSheetVersionAsDTO(int version) {
        return new SheetDTO(this.archive.retrieveFromArchive(version));
    }

    @Override
    public boolean isSheetLoaded() {
        return this.sheet != null;
    }

    @Override
    public void LoadFromFile(String path) {

    }

    @Override
    public void SaveToFile(String path) {
        this.archive.saveToFile(path);
    }
}
