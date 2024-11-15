package ui.menu;

import logic.engine.Engine;
import logic.engine.EngineImpl;
import ui.io.ConsoleUtils;
import user.User;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Menu {
    private MainMenuOption chosenItem;

    public void runMenu(){
        Engine engine = new EngineImpl(new User("User1"));

        do {
            ConsoleUtils.printMainMenu();
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
            System.out.println("Error: input entered was not a whole number.");
        }

        return input > 0 && input < MainMenuOption.values().length ?
                MainMenuOption.values()[input] :
                MainMenuOption.INVALID_CHOICE;
    }
}
