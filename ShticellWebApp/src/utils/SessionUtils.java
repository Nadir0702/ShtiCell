package utils;


import constants.Constants;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

public class SessionUtils {

    public static String getUsername (HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Object sessionAttribute = session != null ? session.getAttribute(Constants.USERNAME) : null;
        return sessionAttribute != null ? sessionAttribute.toString() : null;
    }
    
    public static String getEngineName (HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Object sessionAttribute = session != null ? session.getAttribute(Constants.SHEET_NAME) : null;
        return sessionAttribute != null ? sessionAttribute.toString() : null;
    }
    
    public static boolean isSessionExists (HttpServletResponse response, String username) throws IOException {
        if (username == null) {
            WriteUnauthorizedResponse(response, "User is not logged in");
            return false;
        }
        
        return true;
    }
    
    public static boolean isActiveEngineExists(HttpServletResponse response, String sheetName) throws IOException {
        if (sheetName == null) {
            WriteUnauthorizedResponse(response, "No Active Sheet Found");
            return false;
        }
        
        return true;
    }
    
    private static void WriteUnauthorizedResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().println(message);
        response.getWriter().flush();
    }
    
    public static void clearSession (HttpServletRequest request) {
        request.getSession().invalidate();
    }
}