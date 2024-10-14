package client.gui.home.Command;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.Objects;

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
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PermissionRequestTableEntry that = (PermissionRequestTableEntry) o;
        return Objects.equals(sender.get(), that.sender.get()) && Objects.equals(sheetName.get(), that.sheetName.get()) && Objects.equals(permission.get(), that.permission.get());
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(sender, sheetName, permission);
    }
}
