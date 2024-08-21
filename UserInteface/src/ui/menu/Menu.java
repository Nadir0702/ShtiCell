package ui.menu;

import logic.Engine;
import logic.EngineImpl;
import ui.output.Printer;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Menu {
    private MainMenuOption chosenItem;

    public void runMenu(){
        Engine engine = new EngineImpl();

        do {
            Printer.MAIN_MENU.print();
            this.chosenItem = this.getMainMenuChoiceFromUser();
            this.chosenItem.executeOption(engine);
        } while(true);
    }

    private MainMenuOption getMainMenuChoiceFromUser() {
        Scanner scanner = new Scanner(System.in);
        int input = 0;

        try {
            input = scanner.nextInt();
        }
        catch (InputMismatchException e) {
            System.out.println("Error: input entered was not a number.");
        }

        return input > 0 && input < MainMenuOption.values().length ?
                MainMenuOption.values()[input] :
                MainMenuOption.INVALID_CHOICE;
    }
}
