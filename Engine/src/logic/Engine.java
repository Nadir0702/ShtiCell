package logic;

public interface Engine {
    boolean LoadData(String path);
    void showSheet();
    void showSingleCellData(String cellID);
    void updateSingleCellData(String cellID);
    void showVersions();
}
