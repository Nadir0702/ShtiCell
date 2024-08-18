package ui;

import ui.menu.MainMenuOption;
import ui.menu.Menu;

public class Main {

    public static void main(String[] args) {
        System.out.println("hello shticell");

        Menu mainMenu = new Menu(MainMenuOption.INITIALIZER.getMenuItems());
        mainMenu.runMenu();
    }
}
