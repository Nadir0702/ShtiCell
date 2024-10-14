package client.gui.util;

import com.google.gson.Gson;

public class Constants {
    
    // global constants
    public final static String LINE_SEPARATOR = System.getProperty("line.separator");
    public final static String JHON_DOE = "<Anonymous>";
    public final static int REFRESH_RATE = 2000;
    public final static String CHAT_LINE_FORMATTING = "%tH:%tM:%tS | %.10s: %s%n";
    
    // Server resources locations
    public final static String BASE_DOMAIN = "localhost";
    private final static String BASE_URL = "http://" + BASE_DOMAIN + ":8080";
    private final static String CONTEXT_PATH = "/ShticellWebApp_Web_exploded";
    private final static String FULL_SERVER_PATH = BASE_URL + CONTEXT_PATH;
    
    public final static String LOAD_SHEET = FULL_SERVER_PATH + "/loadSheetFromFile";
    public static final String SEND_PERMISSION_REQUEST = FULL_SERVER_PATH + "/sendPermissionRequest";
    
    public final static String REFRESH_SHEET_TABLE = FULL_SERVER_PATH + "/refreshSheetTable";
    public static final String REFRESH_PERMISSION_TABLE = FULL_SERVER_PATH + "/refreshPermissionTable";
    public static final String REFRESH_PERMISSION_REQUESTS_TABLE = FULL_SERVER_PATH + "/refreshPermissionRequestsTable";
    
    // GSON instance
    public final static Gson GSON_INSTANCE = new Gson();
}
