package ui.menu;

import dto.SheetDTO;
import logic.Engine;
import ui.output.ConsolePrinter;

import javax.print.attribute.standard.MediaSize;

import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

import static java.lang.System.exit;
import static java.lang.System.setOut;

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
            String path = getFilePathFromUser();
            if (engine.LoadData(path)) {
                System.out.println("File Loaded Successfully");
            } else {
                System.out.println("Error: Couldn't load file.");
            }
        }

        private String getFilePathFromUser() {
            Scanner scanner = new Scanner(System.in);

            System.out.println("Please Enter the full path of the file you wish to load:");
            String path = scanner.nextLine();

            while( !isValidPathFormat(path)){
                System.out.println("Please Enter the valid path of the file you wish to load:");
                path = scanner.nextLine();
            }

            return path;
        }

        private boolean isValidPathFormat(String filePath) {
            try {
                Path path = Paths.get(filePath);
                return true; // Path is valid if no exception is thrown
            } catch (InvalidPathException e) {
                System.out.println("The File Path is invalid.");
                return false; // Path is invalid
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
            String cellID = getCellIDFromUser();
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
            String cellID = getCellIDFromUser();
            engine.updateSingleCellData(cellID);

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

    private static String getCellIDFromUser() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Please Enter the cell ID(for example A4):");
        String cellID = scanner.nextLine();

        while (!isValidCellID(cellID)){
            System.out.println("Please Enter the valid cell ID(for example A4):");
            cellID = scanner.nextLine();
        }

        return cellID;
    }

    private static boolean isValidCellID(String cellID) {
        boolean isValid = true;

        if (cellID.isBlank()) {
            System.out.println("Cannot enter an empty cell ID");
            isValid = false;
        }else if (!Character.isUpperCase(cellID.charAt(0))) {
            System.out.println("Please enter column as upper case letter only.");
            isValid = false;
        }else if (!cellID.substring(1).matches("\\d+")) {
            System.out.println("Please enter row as number only");
            isValid = false;
        }

        return isValid;
    }

    public abstract void executeOption(Engine engine);
}
