package ui.menu;

import java.util.List;

public class Menu {

    private final List<MenuItem> menuItems;
    private MenuItem chosenItem;
    private MenuItem exitItem;

    public Menu(List<MenuItem> menuItems) {
        this.menuItems = menuItems;

        for (MenuItem menuItem : menuItems) {
            if (menuItem.getMenuItemName().equals("EXIT")) {
                this.exitItem = menuItem;
            }

            if (menuItem.getMenuItemName().equals("INITIALIZER")) {
                this.chosenItem = menuItem;
            }
        }
    }

    public void runMenu(){
        this.printMenu();
        this.chosenItem = getInputFromUser();
        while(this.chosenItem != this.exitItem){
            chosenItem.executeOption();
            this.printMenu();
            this.chosenItem = getInputFromUser();
        }
    }

    private void printMenu(){
        int i = 1;
        for (MenuItem menuItem : menuItems) {
            if (!menuItem.getMenuItemName().equals("INITIALIZER")) {
                System.out.println( (i++) + ") " + menuItem);
            }
        }
    }

    private MenuItem getInputFromUser(){
        return null;
    }
}
