package component.sheet.api;

public interface Sheet extends ReadonlySheet, UpdatableSheet{
    static boolean isValidCellID(String cellID) {
        boolean isValid = true;

        if (cellID.isBlank()) {
            throw new IllegalArgumentException("Cannot enter an empty cell ID");
        }else if (!Character.isLetter(cellID.charAt(0))) {
            throw new IllegalArgumentException("Column must be a single letter.");
        }else if (!cellID.substring(1).matches("\\d+")) {
            throw new IllegalArgumentException("Row must be whole number bigger than 0.");
        }

        return isValid;
    }
}
