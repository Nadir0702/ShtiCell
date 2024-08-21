package ui.output;

import ui.menu.MainMenuOption;

public enum Printer {
    MAIN_MENU{
        @Override
        public void print() {
            for (MainMenuOption option : MainMenuOption.values()) {
                if(MainMenuOption.INVALID_CHOICE != option) {
                    System.out.println(option.ordinal() + ") " + option);
                }
            }
        }
    },
    SHEET{
        @Override
        public void print() {
//            // Get details from the sheet
//            int numRows = sheet.getNumRows();
//            int numCols = sheet.getNumCols();
//            int colWidth = sheet.getColWidth();
//            String sheetName = sheet.getName();
//            int versionNumber = sheet.getVersionNumber();
//            List<List<String>> cellData = sheet.getCellData(); // Assume this returns a 2D list of cell values
//
//            // Display the sheet name and version
//            System.out.println("Sheet Name: " + sheetName);
//            System.out.println("Version: " + versionNumber);
//            System.out.println();
//
//            // Print the column headers (A, B, C, ...)
//            System.out.print("   "); // For row numbers
//            for (int col = 0; col < numCols; col++) {
//                char colHeader = (char) ('A' + col);
//                System.out.print(padRight(String.valueOf(colHeader), colWidth) + "|");
//            }
//            System.out.println();
//
//            // Print each row
//            for (int row = 0; row < numRows; row++) {
//                System.out.print(padLeft(String.format("%02d", row + 1), 3)); // Print row number with leading zero
//                for (int col = 0; col < numCols; col++) {
//                    String cellValue = cellData.get(row).get(col);
//                    if (cellValue == null) {
//                        cellValue = "";
//                    }
//                    System.out.print(padRight(cellValue, colWidth) + "|");
//                }
//                System.out.println();
//            }
        }

        // Helper method to pad a string to the right to a given length
        private String padRight(String s, int n) {
            return String.format("%-" + n + "s", s);
        }

        // Helper method to pad a string to the left to a given length
        private String padLeft(String s, int n) {
            return String.format("%" + n + "s", s);
        }

    },
    CELL{
        @Override
        public void print() {

        }
    },
    SIMPLIFIED_CELL{
        @Override
        public void print() {

        }
    };

    public abstract void print();
}
