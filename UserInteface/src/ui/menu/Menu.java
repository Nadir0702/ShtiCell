package ui.menu;

import ui.io.api.Input;
import ui.io.api.Output;
import ui.io.impl.InputImpl;
import ui.io.impl.OutputImpl;

public class Menu {
    private Input input;
    private Output output;
    private MainMenuOption chosenItem;

    public Menu() {
        this.input = new InputImpl();
        this.output = new OutputImpl();
    }

    public void runMenu(){

        do {
            this.output.printMainMenu();
            this.chosenItem = this.input.getMainMenuChoiceFromUser();
            this.chosenItem.executeOption();
        } while(true);
    }
}
