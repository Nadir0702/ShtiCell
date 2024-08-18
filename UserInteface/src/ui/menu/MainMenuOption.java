package ui.menu;

import ui.io.api.Output;
import ui.io.impl.OutputImpl;

import java.util.Arrays;
import java.util.List;

import static java.lang.System.exit;

public enum MainMenuOption {
    INVALID_CHOICE{
        private Output printer = new OutputImpl();

        @Override
        public void executeOption() {
            printer.printInvalidMenuOption();
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
            exit(0);
        }

        @Override
        public String toString() {
            return "Exit Shticell";
        }
    };

    public abstract void executeOption();
}
