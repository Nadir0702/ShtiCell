package ui.io.impl;

import ui.io.api.Output;
import ui.menu.MainMenuOption;

public class OutputImpl implements Output {
    @Override
    public void printMainMenu() {
        int i = 1;

        for (MainMenuOption option : MainMenuOption.values()) {
            if(MainMenuOption.INVALID_CHOICE != option) {
                System.out.println( (i++) + ") " + option);
            }
        }
    }

    @Override
    public void printInputMismatch() {
        System.out.println("Error: input entered was not a number.");
    }

    @Override
    public void printInvalidMenuOption() {
        System.out.println("No such menu option.");
        this.printTryAgain();
    }

    @Override
    public void printTryAgain() {
        System.out.println("Please Try Again:");
    }
}
