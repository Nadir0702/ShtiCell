package ui.menu;

import component.sheet.api.Sheet;
import dto.CellDTO;
import dto.SheetDTO;
import logic.Engine;
import ui.output.ConsolePrinter;

import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.lang.System.exit;

public enum MainMenuOption {
    INVALID_CHOICE{
        @Override
        public void executeOption(Engine engine) {
            System.out.println("No such menu option, Please Try Again:");
        }
    },
    LOAD_XML_FILE{
        @Override
        public void executeOption(Engine engine) {
            try {
                String path = ConsolePrinter.getInputFromUser("Please Enter the full path of the file you wish to load:", this::isValidPathFormat);
                engine.LoadData(path);
                System.out.println("File Loaded Successfully");
            } catch (RuntimeException e) {
                System.out.println("Error Loading File:\n" + e.getMessage() + "\n");
            }
        }

        public boolean isValidPathFormat(String filePath) {
            Path path = Paths.get(filePath);
            boolean isValid = true;
            int suffixIndex;

            if (!Files.exists(path)) {
                System.out.println("The File " + filePath + " does not exist.");
                isValid = false;
            } else if ((suffixIndex = path.getFileName().toString().lastIndexOf(".")) == -1){
                System.out.println("The file " + filePath + " is not a valid XML file");
                isValid = false;
            } else if (path.getFileName().toString().substring(suffixIndex).equals("xml")){
                System.out.println("The file " + filePath + " is not a valid XML file");
                isValid = false;
            }

            return isValid;
        }

        @Override
        public String toString() {
            return "Load Sheet From XML File";

        }
    },
    SHOW_SHEET{
        @Override
        public void executeOption(Engine engine) {
            ConsolePrinter.printSheet(engine.getSheetAsDTO());
        }

        @Override
        public String toString() {
            return "Show Sheet Details";
        }
    },
    SHOW_SINGLE_CELL{
        @Override
        public void executeOption(Engine engine) {
            try {
                String cellID = ConsolePrinter.getInputFromUser("Please Enter the cell ID(for example A4):", Sheet::isValidCellID);
                CellDTO cellDTO = engine.getSingleCellData(cellID);
                if (cellDTO.isActive()) {
                    ConsolePrinter.printCell(cellDTO);
                } else {
                    System.out.println("The cell " + cellID + " has no value yet.");
                }
            } catch (RuntimeException e) {
                System.out.println("Error Finding Cell:\n" + e.getMessage() + "\n");
            }

        }

        @Override
        public String toString() {
            return "Show Single Cell Details";
        }
    },
    UPDATE_SINGLE_CELL{
        @Override
        public void executeOption(Engine engine) {
            try {
                String cellID = ConsolePrinter.getInputFromUser("Please Enter the cell ID(for example A4):", Sheet::isValidCellID);
                CellDTO cellDTO = engine.getSingleCellData(cellID);

                if (cellDTO.isActive()) {
                    ConsolePrinter.printSimplifiedCell(cellDTO);
                } else {
                    System.out.println("The cell " + cellID + " has no value yet.");
                }

                String newOriginalValue = ConsolePrinter.getOriginalValueFromUser(cellID);
                engine.updateSingleCellData(cellID, newOriginalValue);
                SHOW_SHEET.executeOption(engine);
            } catch (RuntimeException e) {
                System.out.println("Error Updating Cell:\n" + e.getMessage() + "\n");
            }
        }

        @Override
        public String toString() {
            return "Update Single Cell Value";
        }
    },
    SHOW_VERSIONS{
        @Override
        public void executeOption(Engine engine) {

        }

        @Override
        public String toString() {
            return "Show Sheet Versions";
        }
    },
    EXIT{
        @Override
        public void executeOption(Engine engine) {
            exit(0);
        }

        @Override
        public String toString() {
            return "Exit Shticell";
        }
    };

    public abstract void executeOption(Engine engine);
}
