package ui.menu;

import java.util.Arrays;
import java.util.List;

public enum MainMenuOption implements MenuItem {
    INITIALIZER{
        @Override
        public void executeOption() {}

        @Override
        public String toString()
        {
            return "Initializer";
        }
    },
    LOAD_XML_FILE{
        @Override
        public void executeOption() {

        }

        @Override
        public String toString() {
            return "Load Sheet From XML File";

        }
    },
    SHOW_SHEET{
        @Override
        public void executeOption() {

        }

        @Override
        public String toString() {
            return "Show Sheet Details";
        }
    },
    SHOW_SINGLE_CELL{
        @Override
        public void executeOption() {

        }

        @Override
        public String toString() {
            return "Show Single Cell Details";
        }
    },
    UPDATE_SINGLE_CELL{
        @Override
        public void executeOption() {

        }

        @Override
        public String toString() {
            return "Update Single Cell Value";
        }
    },
    SHOW_VERSIONS{
        @Override
        public void executeOption() {

        }

        @Override
        public String toString() {
            return "Show Sheet Versions";
        }
    },
    EXIT{
        @Override
        public void executeOption() {

        }

        @Override
        public String toString() {
            return "Exit Shticell";
        }
    };

    @Override
    public List<MenuItem> getMenuItems() {
        return Arrays.asList(values());
    }

    @Override
    public String getMenuItemName() {
        return this.name();
    }
}
