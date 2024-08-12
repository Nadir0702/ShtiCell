package component;

import java.util.Map;

public class Sheet {
    private String sheetName;
    private String xmlPath;
    private Layout layout;
    private Map<String, Cell> location2Cell;
    private int version;

    private class Layout {
        private int row;
        private int column;
        private int rowHeight;
        private int columnWidth;

    }
}
