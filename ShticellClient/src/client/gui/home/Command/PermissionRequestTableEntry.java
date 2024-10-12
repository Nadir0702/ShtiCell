package client.gui.home.Command;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class PermissionRequestTableEntry {
    private StringProperty sender;
    private StringProperty sheetName;
    private StringProperty permission;
    
    public PermissionRequestTableEntry(String sender, String sheetName, String permission) {
        this.sender = new SimpleStringProperty(sender);
        this.sheetName = new SimpleStringProperty(sheetName);
        this.permission = new SimpleStringProperty(permission);
    }
    
    public String getSender() { return this.sender.get(); }
    
    public String getSheetName() { return this.sheetName.get(); }
    
    public String getPermission() { return this.permission.get(); }
}
