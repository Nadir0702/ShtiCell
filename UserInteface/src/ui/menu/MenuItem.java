package ui.menu;

import java.util.List;

public interface MenuItem {

    void executeOption();

    List<MenuItem> getMenuItems();

    String getMenuItemName();

}
