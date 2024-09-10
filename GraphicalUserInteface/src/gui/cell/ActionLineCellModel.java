package gui.cell;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;

public interface ActionLineCellModel {
    void bind(StringProperty cellIDProperty, StringProperty originalValueProperty, StringProperty lastUpdatedVersionProperty);
    StringProperty getCellIDProperty();
    StringProperty getOriginalValueProperty();
    StringProperty getLastUpdatedVersionProperty();
    
}
