package ui.menu;

import component.sheet.api.Sheet;
import dto.SheetDTO;
import logic.Engine;
import ui.output.ConsolePrinter;

import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.function.Predicate;

import static component.sheet.api.Sheet.isValidCellID;
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
            String path = ConsolePrinter.getInputFromUser("Please Enter the full path of the file you wish to load:", MainMenuOption::isValidPathFormat);
            if (engine.LoadData(path)) {
                System.out.println("File Loaded Successfully");
            } else {
                System.out.println("Error: Couldn't load file.");
            }
        }

        @Override
        public String toString() {
            return "Load Sheet From XML File";

        }
    },
    SHOW_SHEET{
        @Override
        public void executeOption(Engine engine) {
            SheetDTO sheetDTO = engine.getSheetAsDTO();
            ConsolePrinter.printSheet(sheetDTO);
        }

        @Override
        public String toString() {
            return "Show Sheet Details";
        }
    },
    SHOW_SINGLE_CELL{
        @Override
        public void executeOption(Engine engine) {
            String cellID = ConsolePrinter.getInputFromUser("Please Enter the cell ID(for example A4):", Sheet::isValidCellID);
            engine.getSingleCellData(cellID);
        }

        @Override
        public String toString() {
            return "Show Single Cell Details";
        }
    },
    UPDATE_SINGLE_CELL{
        @Override
        public void executeOption(Engine engine) {
            String cellID = ConsolePrinter.getInputFromUser("Please Enter the cell ID(for example A4):", Sheet::isValidCellID);
            engine.updateSingleCellData(cellID, "hello");

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


    public  static boolean isValidPathFormat(String filePath) {
        try {
            Path path = Paths.get(filePath);
            return true; // Path is valid if no exception is thrown
        } catch (InvalidPathException e) {
            System.out.println("The File Path is invalid.");
            return false; // Path is invalid
        }
    }


    public abstract void executeOption(Engine engine);
}
