package ui.output;

import dto.CellDTO;
import dto.SheetDTO;
import logic.function.returnable.api.Returnable;
import ui.menu.MainMenuOption;

import java.util.Map;
import java.util.Scanner;
import java.util.function.Predicate;

public class ConsolePrinter {
    public static void printMainMenu() {
        System.out.println("Please Choose The Option Number out of the Following Options:");
        for (MainMenuOption option : MainMenuOption.values()) {
            if(MainMenuOption.INVALID_CHOICE != option) {
                System.out.println(option.ordinal() + ") " + option);
            }
        }
    }

    public static String getInputFromUser(String messageToUser, Predicate<String> inputValidationMethod) {
        Scanner scanner = new Scanner(System.in);

        System.out.println(messageToUser);
        String input = scanner.nextLine();

        while (!inputValidationMethod.test(input)){
            System.out.println(messageToUser);
            input = scanner.nextLine();
        }

        return input;
    }

    public static void printSheet(SheetDTO sheet) {
        // Get details from the sheet
        int numRows = sheet.getLayout().getRow();
        int numCols = sheet.getLayout().getColumn();
        int colWidth = sheet.getLayout().getColumnWidth();
        String sheetName = sheet.getSheetName();
        int versionNumber = sheet.getVersion();
        Map<String, Returnable> activeCells = sheet.getCells(); // Assume this is the map of active cells

        // Display the sheet name and version
        System.out.println("Sheet Name: " + sheetName);
        System.out.println("Version: " + versionNumber);
        System.out.println();

        // Print the column headers (A, B, C, ...)
        System.out.print("   |"); // For row numbers with a separator
        for (int col = 0; col < numCols; col++) {
            char colHeader = (char) ('A' + col);
            System.out.print(centerText(String.valueOf(colHeader), colWidth) + "|");
        }
        System.out.println();

        // Print each row
        for (int row = 0; row < numRows; row++) {
            System.out.print(padLeft(String.format("%02d ", row + 1), 3) + "|"); // Print row number with leading zero and separator
            for (int col = 0; col < numCols; col++) {
                char colHeader = (char) ('A' + col);
                String cellKey = colHeader + String.valueOf(row + 1);
                String cellValue = "";

                if (activeCells.containsKey(cellKey)) {
                    cellValue = activeCells.get(cellKey).getValue().toString();
                }

                System.out.print(padRight(cellValue, colWidth) + "|");
            }
            System.out.println();
        }
        System.out.println();
    }

    // Helper method to center text within a given width
    private static String centerText(String s, int width) {
        int padding = (width - s.length()) / 2;
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < padding; i++) {
            result.append(" ");
        }
        result.append(s);
        while (result.length() < width) {
            result.append(" ");
        }
        return result.toString();
    }

    // Helper method to pad a string to the right to a given length
    private static String padRight(String s, int n) {
        return String.format("%-" + n + "s", s);
    }

    // Helper method to pad a string to the left to a given length
    private static String padLeft(String s, int n) {
        return String.format("%" + n + "s", s);
    }

    public static void printSimplifiedCell(CellDTO cellDTO){
        System.out.println("Cell ID: " + cellDTO.getCellId());
        System.out.println("Original Value: " + cellDTO.getOriginalValue());
        System.out.println("Effective Value: " + cellDTO.getEffectiveValue());
    }

    public static void printCell(CellDTO cellDTO) {
        printSimplifiedCell(cellDTO);
        System.out.println("Last Modified Version: " + cellDTO.getVersion());

        System.out.println("Dependencies: ");
        if (cellDTO.getDependingOn() != null && !cellDTO.getDependingOn().isEmpty()) {
            for (String dep : cellDTO.getDependingOn()) {
                System.out.println(" - " + dep);
            }
        } else {
            System.out.println(" None");
        }

        System.out.println("Dependents: ");
        if (cellDTO.getInfluencingOn() != null && !cellDTO.getInfluencingOn().isEmpty()) {
            for (String dep : cellDTO.getInfluencingOn()) {
                System.out.println(" - " + dep);
            }
        } else {
            System.out.println(" None");
        }
    }

    public static String getOriginalValueFromUser(String cellID) {
        System.out.println("Please Enter the new Value for cell " + cellID + ":");
        System.out.println("The new Value can be of the Following Types:");
        System.out.println(" - Number (positive, negative or floating point)");
        System.out.println(" - Boolean (TRUE/FALSE - Capital Letters Only)");
        System.out.println(" - String (string of any kind)");
        System.out.println(" - Function (Must be in the Following format: {NAME,argument1,argument2}");
        System.out.println("   function name must be capital letters only)");
        System.out.println("   function examples: {PLUS,{REF,A3},4} or {CONCAT, hello, world}");
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }
}
