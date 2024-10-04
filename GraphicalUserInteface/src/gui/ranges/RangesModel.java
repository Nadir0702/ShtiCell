package gui.ranges;

import dto.RangeDTO;
import javafx.collections.ObservableList;

public interface RangesModel {
    ObservableList<RangeDTO> rangesProperty();
    void deleteRange(RangeDTO rangeDTO);
    RangeDTO getSelectedRange();
    void setSelectedRange(RangeDTO rangeDTO);
}
