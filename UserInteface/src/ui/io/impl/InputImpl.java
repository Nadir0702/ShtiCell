package ui.io.impl;

import ui.io.api.Input;
import ui.io.api.Output;
import ui.menu.MainMenuOption;

import java.util.InputMismatchException;
import java.util.Scanner;

public class InputImpl implements Input {
    private Scanner scanner;
    private Output printer;

    public InputImpl()
    {
        scanner = new Scanner(System.in);
        printer = new OutputImpl();
    }

    @Override
    public MainMenuOption getMainMenuChoiceFromUser() {
        int input = 0;

        try {
            input = scanner.nextInt();
        }
        catch (InputMismatchException e) {
            this.printer.printInputMismatch();
        }

        return input > 0 && input < MainMenuOption.values().length ? MainMenuOption.values()[input] : MainMenuOption.INVALID_CHOICE;
    }
}
