package logic;

import logic.engine.Engine;

import java.util.*;

public class EngineManager {
    private final Map<String, Engine> enginesMap;
    
    public EngineManager() {
        this.enginesMap = new LinkedHashMap<>();
    }
    
    public synchronized void addEngine(String sheetName, Engine engine) {
        enginesMap.put(sheetName, engine);
    }
    
    public synchronized void removeEngine(String sheetName) {
        enginesMap.remove(sheetName);
    }
    
    public synchronized Map<String, Engine> getEngines() {
        return Collections.unmodifiableMap(enginesMap);
    }
    
    public boolean isEngineExists(String sheetName) {
        return this.enginesMap.containsKey(sheetName);
    }
}
