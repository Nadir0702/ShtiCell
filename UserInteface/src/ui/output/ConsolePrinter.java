package ui.output;

import dto.SheetDTO;
import logic.function.returnable.api.Returnable;
import ui.menu.MainMenuOption;

import java.util.Map;

public class ConsolePrinter {
    public static void printMainMenu() {
        for (MainMenuOption option : MainMenuOption.values()) {
            if(MainMenuOption.INVALID_CHOICE != option) {
                System.out.println(option.ordinal() + ") " + option);
            }
        }
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
}